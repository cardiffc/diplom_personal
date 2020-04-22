package services;

import enums.ModerationStatus;
import enums.PostSortTypes;
import enums.VoteType;
import model.Post;
import model.PostVote;
import repositories.PostRepository;
import response.PostBody;
import response.PostResponseBody;
import response.UserBody;
import services.comparators.PostByCommentComp;
import services.comparators.PostByDateComp;
import services.comparators.PostLikeComp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class PostService {

    public PostResponseBody getPostResponse(PostRepository postRepository, int offset, int limit, String mode) {

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

        List<PostBody> postBodies = new ArrayList<>();

        int finish = Math.min(sortedPosts.size(), offset + limit);
        for (int i = offset; i < finish; i++) {
            Post currentPost = sortedPosts.get(i);
            UserBody userBody = new UserBody(
                    currentPost.getUser().getId(),
                    currentPost.getUser().getName()
            );
            PostBody postBody = new PostBody(
                    currentPost.getId(),
                    currentPost.getTime().toString(),
                    userBody,
                    currentPost.getTitle(),
                    //TODO: тут должен быть annonce
                    currentPost.getTitle(),
                    currentPost.getVotes(VoteType.like),
                    currentPost.getVotes(VoteType.dislike),
                    currentPost.getCommentsCount(),
                    currentPost.getViewCount()
            );

            postBodies.add(postBody);

        }

        return new PostResponseBody(count, postBodies);
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
}
