package services;

import enums.VoteType;
import model.Post;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repositories.UserRepository;
import response.StatiscticsResponse;
import services.intefraces.QueryConditions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;


@Component
public class GeneralService implements QueryConditions {

    @Autowired
    private PostService postService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    public StatiscticsResponse getMyStatistics (int userId) {
        User user = userRepository.findById(userId);
        Query postsQuery = entityManager.createQuery(allPostCond + "and p.user = :user")
                .setParameter("user", user).setParameter("today", LocalDateTime.now());
        List<Post> posts = postsQuery.getResultList();
        return createStatisticsResponse(posts);
    }

    //TODO Доделать связку с globalsettings когда они будут реализованы
    public StatiscticsResponse getAllStatistics () {
        Query postsQuery = entityManager.createQuery(allPostCond).setParameter("today", LocalDateTime.now());
        List<Post> posts = postsQuery.getResultList();
        return createStatisticsResponse(posts);
    }

    private StatiscticsResponse createStatisticsResponse (List<Post> posts) {
        int postsCount = posts.size();
        int likesCount = 0;
        int dislikesCount = 0;
        int viewsCount = 0;
        for (Post post : posts) {
            likesCount += post.getVotes(VoteType.like);
            dislikesCount += post.getVotes(VoteType.dislike);
            viewsCount += post.getViewCount();
        }
        List<Post> sortedPosts = postService.getSortedPosts(posts, "early");
        String firstPublication = sortedPosts.get(0).getTime().toString();
        return StatiscticsResponse.builder().postsCount(postsCount).likesCount(likesCount).dislikesCount(dislikesCount)
                .firstPublication(firstPublication).viewsCount(viewsCount).build();
    }

}
