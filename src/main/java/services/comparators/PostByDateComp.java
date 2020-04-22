package services.comparators;
import model.Post;
import java.util.Comparator;

public class PostByDateComp implements Comparator<Post> {
    @Override
    public int compare(Post post1, Post post2)
    {
        return post2.getTime().compareTo(post1.getTime());


    }
}
