package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import response.PostResponseBody;
import response.PostsResponseBody;
import services.PostService;

import java.text.ParseException;

@Controller
@RequestMapping("/api/post")
public class ApiPostController {


    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<PostsResponseBody> getPost(int offset, int limit, String mode) {
        PostsResponseBody postResponseBody = postService.getPostResponse(offset, limit, mode);
        return new ResponseEntity<>(postResponseBody, HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<PostsResponseBody> getSearchedPost(int offset, int limit, String query) {
        PostsResponseBody postResponseBody = postService.getSearchedPosts(offset, limit, query);
        return new ResponseEntity<>(postResponseBody, HttpStatus.OK);
    }



    @GetMapping("byDate")
    public ResponseEntity<PostsResponseBody> getPostsByDate(int offset, int limit, String date) throws ParseException {
        PostsResponseBody postResponseBody = postService.getPostsByDate(offset,limit,date);
        return new ResponseEntity<>(postResponseBody, HttpStatus.OK);
    }

    @GetMapping("byTag")
    public ResponseEntity<PostsResponseBody> getPostsByTag(int offset, int limit, String tag) {
        PostsResponseBody postResponseBody = postService.getPostsByTag(offset,limit,tag);
        return new ResponseEntity<>(postResponseBody, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<PostResponseBody> getPostById(@PathVariable int id) {
        PostResponseBody currentPostResponseBody = postService.getPostById(id);
        return new ResponseEntity<>(currentPostResponseBody, HttpStatus.OK);
    }
}
