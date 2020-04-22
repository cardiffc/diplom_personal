package controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import repositories.PostRepository;
import response.PostResponseBody;
import services.PostService;

@Controller
public class ApiPostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/api/post")
    public ResponseEntity<PostResponseBody> getPost(int offset,int limit,String mode) {
        PostService postService = new PostService();
        PostResponseBody postResponseBody = postService.getPostResponse(postRepository, offset, limit, mode);
        return new ResponseEntity<PostResponseBody>(postResponseBody, HttpStatus.OK);
    }
}
