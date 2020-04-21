package services;

import lombok.Builder;
import model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import repositories.PostRepository;
import response.PostBody;
import response.PostResponseBody;
import response.UserBody;

import java.util.ArrayList;

import java.util.List;


public class PostService {


    public PostResponseBody getPostResponse(PostRepository repository) {
        Iterable<Post> posts = repository.findAll();
        List<Post> allPosts = new ArrayList<>();
        posts.forEach(allPosts::add);
        int postsCount = allPosts.size();
        List<PostBody> postBodies = new ArrayList<>();
        allPosts.forEach(post -> {
            UserBody userBody = new UserBody(
                    post.getUser().getId(),
                    post.getUser().getName()

            );


            //TODO Сделать нормальный likeCount и announce
            PostBody postBody = new PostBody(
                    post.getId(),
                    "Вчера, 17:25",
                    userBody,
                    post.getTitle(),
                    "Без тэгов",
                    12,
                    1,
                    2,
                    3

            );
            postBodies.add(postBody);
        });
        return new PostResponseBody(postsCount, postBodies);

    }
}
