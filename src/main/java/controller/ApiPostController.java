package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import response.PostResponseBody;
import services.PostService;

import java.text.ParseException;

@Controller
@RequestMapping("/api/post")
public class ApiPostController {


    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<PostResponseBody> getPost(int offset,int limit,String mode) {
        PostResponseBody postResponseBody = postService.getPostResponse(offset, limit, mode);
        return new ResponseEntity<>(postResponseBody, HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<PostResponseBody> getSearchedPost(int offset, int limit, String query) {
        PostResponseBody postResponseBody = postService.getSearchedPosts(offset, limit, query);
        return new ResponseEntity<>(postResponseBody, HttpStatus.OK);
    }


    @GetMapping("{id}")
    public ResponseEntity<PostResponseBody> getCurrentPost(@PathVariable int id) {
        return null;
    }

    @GetMapping("byDate")
    public ResponseEntity<PostResponseBody> getPostsByDate(int offset, int limit, String date) throws ParseException {
        PostResponseBody postResponseBody = postService.getPostsByDate(offset,limit,date);
        return new ResponseEntity<>(postResponseBody, HttpStatus.OK);
    }

    @GetMapping("byTag")
    public ResponseEntity<PostResponseBody> getPostsByTag(int offset, int limit, String tag) {
        PostResponseBody postResponseBody = postService.getPostsByTag(offset,limit,tag);
        return new ResponseEntity<>(postResponseBody, HttpStatus.OK);
    }
}
