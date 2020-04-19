package processor;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.Post;

import java.util.ArrayList;
import java.util.Map;

@Data
@AllArgsConstructor
public class AllPosts {
    private int count;
    //private Map<String, String> posts;
    private ArrayList<Post> newList;

}
