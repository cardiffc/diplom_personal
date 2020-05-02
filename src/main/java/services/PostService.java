package services;

import enums.ModerationStatus;
import enums.PostSortTypes;
import enums.VoteType;
import model.Post;
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



    public PostResponseBody getPostResponse(int offset, int limit, String mode) {

        // Revieving all posts from database and adding to collections with active condition
        Iterable<Post> postIterable = postRepository.findAll();
        List<Post> posts = new ArrayList<>();
        int count = 0;
        for (Post post : postIterable) {
            count++;
            if (post.getIsActive() == 1
                    && post.getModerationStatus().equals(ModerationStatus.ACCEPTED)
                    && post.getTime().isBefore(LocalDateTime.now())) {
                posts.add(post);
            }
        }
        List<Post> sortedPosts = getSortedPosts(posts, mode);
        return createResponse(count, limit, offset, sortedPosts);
    }

    public PostResponseBody getSearchedPosts (int offset, int limit, String query) {
        if (query.trim().equals("")) {
            return  getPostResponse(offset, limit, PostSortTypes.recent.toString());
        }
        Query searchQuery = entityManager.createQuery("from Post p where p.text like :searchParam " +
                "and p.moderationStatus = 'ACCEPTED' and p.isActive = :isactive and time < :nowTime", Post.class);
        searchQuery.setParameter("searchParam","%" + query + "%").setParameter("isactive", (byte) 1)
                .setParameter("nowTime", LocalDateTime.now());
        List<Post> foundPosts = searchQuery.getResultList();
        return createResponse(foundPosts.size(), limit, offset, foundPosts);
    }

    public PostResponseBody getPostsByDate(int offset, int limit, String date) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Query searchByDateQuery = entityManager.createQuery(
                "from Post p where p.moderationStatus = 'ACCEPTED' and p.isActive = :isActive and " +
                        "date_trunc('day', p.time) = :day", Post.class);
        searchByDateQuery.setParameter("isActive", (byte) 1).setParameter("day", formatter.parse(date));
        List<Post> foundPosts = searchByDateQuery.getResultList();
        return createResponse(foundPosts.size(), limit, offset, foundPosts);
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

    private PostResponseBody createResponse (int count, int limit, int offset, List<Post> posts) {

        List<PostBody> postBodies = new ArrayList<>();

        int finish = Math.min(posts.size(), offset + limit);
        for (int i = offset; i < finish; i++) {
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
