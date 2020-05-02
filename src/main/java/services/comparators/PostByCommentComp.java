package services.comparators;

import model.Post;

import java.util.Comparator;

public class PostByCommentComp implements Comparator<Post> {
    @Override
    public int compare(Post post, Post t1) {
        return t1.getCommentsCount() - post.getCommentsCount();
    }
}
