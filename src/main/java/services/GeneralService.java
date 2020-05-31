package services;

import enums.ModerationStatus;
import enums.VoteType;
import model.Post;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repositories.PostRepository;
import repositories.UserRepository;
import response.StatiscticsResponse;
import services.intefraces.QueryConditions;
import java.time.LocalDateTime;
import java.util.List;


@Component
public class GeneralService implements QueryConditions {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public StatiscticsResponse getMyStatistics (int userId) {
        User user = userRepository.findById(userId);
        List<Post> posts = postRepository.findPostByModerationStatusAndIsActiveAndTimeBeforeAndUser
                (ModerationStatus.ACCEPTED, (byte) 1, LocalDateTime.now(), user);
        return createStatisticsResponse(posts);
    }

    //TODO Доделать связку с globalsettings когда они будут реализованы
    public StatiscticsResponse getAllStatistics () {
        List<Post> posts = postRepository.findPostByModerationStatusAndIsActiveAndTimeBefore
                (ModerationStatus.ACCEPTED, (byte) 1, LocalDateTime.now());
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
