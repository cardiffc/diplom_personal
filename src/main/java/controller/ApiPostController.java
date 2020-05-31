package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import repositories.UserRepository;
import request.PostRequestBody;
import response.GenericBooleanResponse;
import response.PostResponseBody;
import response.PostsResponseBody;
import services.AuthService;
import services.PostService;

import java.text.ParseException;

@Controller
@RequestMapping("/api/post")
public class ApiPostController {


    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity saveNewPost(@RequestBody PostRequestBody postRequestBody) {
        if (!authService.isUserAuthorized())
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        int userId = authService.getLoggedUserId();
        GenericBooleanResponse genericBooleanResponse = postService.saveNewPost(postRequestBody, userId);
        return new ResponseEntity(genericBooleanResponse,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PostsResponseBody> getPost(int offset, int limit, String mode) {
        PostsResponseBody postResponseBody = postService.getPostResponse(offset, limit, mode);
        return new ResponseEntity<>(postResponseBody, HttpStatus.OK);
    }

    @GetMapping("my")
    public ResponseEntity getMyPosts(int offset, int limit, String status) {
        if (!authService.isUserAuthorized())
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        int userId = authService.getLoggedUserId();
        PostsResponseBody postsResponseBody = postService.getMyPosts(offset, limit, status, userId);
        return new ResponseEntity(postsResponseBody, HttpStatus.OK);

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

    @GetMapping("moderation")
    public ResponseEntity getPostsForModeration(int offset, int limit, String status) {
        if (!authService.isUserAuthorized())
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        int userId = authService.getLoggedUserId();
        if (userRepository.findById(userId).getIsModerator() == 0)
            return new ResponseEntity("User not moderator", HttpStatus.BAD_REQUEST);
        PostsResponseBody postsResponseBody = postService.getPostsForModeration(offset, limit, status, userId);
            return new ResponseEntity(postsResponseBody, HttpStatus.OK);

    }
}
