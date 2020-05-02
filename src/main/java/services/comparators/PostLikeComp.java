package services.comparators;

import enums.VoteType;
import model.Post;

import java.util.Comparator;

public class PostLikeComp implements Comparator<Post> {

    @Override
    public int compare(Post post, Post t1) {
        return t1.getVotes(VoteType.like) - post.getVotes(VoteType.like);

    }
}
