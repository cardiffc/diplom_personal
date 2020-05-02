package services;

import enums.ModerationStatus;
import enums.PostSortTypes;
import enums.VoteType;
import model.Post;
import org.apache.tomcat.util.net.jsse.JSSEUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repositories.PostRepository;
import response.PostBody;
import response.PostResponseBody;
import response.UserBody;
import services.comparators.PostByCommentComp;
import services.comparators.PostByDateComp;
import services.comparators.PostLikeComp;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class PostService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    PostRepository postRepository;

    private static String countPrefix = "select count(*) ";

    public PostResponseBody getPostResponse(int offset, int limit, String mode) {
        Query allPosts = entityManager.createQuery("from Post p where p.moderationStatus = 'ACCEPTED' and " +
                                                        "p.isActive = 1 and time < :nowTime", Post.class);
        allPosts.setParameter("nowTime", LocalDateTime.now());
        int count = (int) entityManager.createQuery("SELECT max(id) from Post").getSingleResult();
        allPosts.setFirstResult(offset);
        allPosts.setMaxResults(limit);
        List<Post> resultPosts = getSortedPosts(allPosts.getResultList(), mode);
        return createResponse(count, resultPosts);
    }

    public PostResponseBody getSearchedPosts (int offset, int limit, String query) {
        if (query.trim().equals("")) {
            return  getPostResponse(offset, limit, PostSortTypes.recent.toString());
        }
        String mainQuery = "from Post p where p.text like :searchParam and p.moderationStatus = 'ACCEPTED' and " +
                            "p.isActive = '1' and time < :nowTime";
        Query countQuery = entityManager.createQuery(countPrefix + mainQuery);
        countQuery.setParameter("searchParam","%" + query + "%").setParameter("nowTime", LocalDateTime.now());
        Long preCount = (Long) countQuery.getSingleResult();
        Integer count = Integer.parseInt(preCount.toString());


        Query searchQuery = entityManager.createQuery(mainQuery, Post.class);
        searchQuery.setParameter("searchParam","%" + query + "%").setParameter("nowTime", LocalDateTime.now());
        searchQuery.setFirstResult(offset);
        searchQuery.setMaxResults(limit);
        List<Post> resultPosts = searchQuery.getResultList();
        return createResponse(count,resultPosts);
    }

    public PostResponseBody getPostsByDate(int offset, int limit, String date) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date day = formatter.parse(date);

        String mainQuery = "from Post p where p.moderationStatus = 'ACCEPTED' and p.isActive = '1' " +
                            "and date_trunc('day', p.time) = :day";
        Query countQuery = entityManager.createQuery(countPrefix + mainQuery);
        countQuery.setParameter("day",day);

        Long preCount = (Long) countQuery.getSingleResult();
        Integer count = Integer.parseInt(preCount.toString());

        Query searchByDateQuery = entityManager.createQuery(
                mainQuery, Post.class);
        searchByDateQuery.setParameter("day", day);
        searchByDateQuery.setFirstResult(offset);
        searchByDateQuery.setMaxResults(limit);
        List<Post> resultPosts = searchByDateQuery.getResultList();
        return createResponse(count,resultPosts);
    }

    public PostResponseBody getPostsByTag(int offset, int limit, String tag) {
        int count = (int) entityManager.createQuery("SELECT max(id) from Post").getSingleResult();

        return null;
    }



    private List<Post> getSortedPosts(List<Post> posts, String mode) {

        if (mode.equals(PostSortTypes.best.toString()))
            posts.sort(new PostLikeComp());
        if (mode.equals(PostSortTypes.popular.toString()))
            posts.sort(new PostByCommentComp());
        if (mode.equals(PostSortTypes.early.toString()))
            posts.sort(new PostByDateComp().reversed());
        if (mode.equals(PostSortTypes.recent.toString()))
            posts.sort(new PostByDateComp());

        return posts;
    }

    private PostResponseBody createResponse (int count, List<Post> posts) {

        List<PostBody> postBodies = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            Post currentPost = posts.get(i);
            UserBody userBody = UserBody.builder().id(currentPost.getUser().getId())
                    .name(currentPost.getUser().getName()).build();

            String postText = currentPost.getText();
            String announce = (postText.length() > 500) ? postText.substring(0,433).concat("...") : postText;
            PostBody postBody = new PostBody(
                    currentPost.getId(),
                    currentPost.getTime().toString(),
                    userBody,
                    currentPost.getTitle(),
                    announce,
                    currentPost.getVotes(VoteType.like),
                    currentPost.getVotes(VoteType.dislike),
                    currentPost.getCommentsCount(),
                    currentPost.getViewCount()
            );
            postBodies.add(postBody);
        }
        return new PostResponseBody(count, postBodies);
    }
}
