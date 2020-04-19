package ru.cardiffc.blog.processor;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cardiffc.blog.model.Post;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class AllPosts {
    private int count;
    //private Map<String, String> posts;
    private ArrayList<Post> newList;

}
