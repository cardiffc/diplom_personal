package controller;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import repositories.PostRepository;
import response.PostResponseBody;
import services.PostService;

@Controller
public class ApiPostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/api/post")
    public ResponseEntity<String> getPost(int offset,int limit,String mode) {

        PostService postService = new PostService();
        PostResponseBody postResponseBody = postService.getPostResponse(postRepository);

        String jsonFile = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().
                        create().toJson(postResponseBody);
        return new ResponseEntity(postResponseBody, HttpStatus.OK);
    }
}
