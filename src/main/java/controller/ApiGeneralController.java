package controller;

import model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import repositories.PostRepository;

@Controller
public class ApiGeneralController {

    @Autowired
    PostRepository postRepository;

    @GetMapping("/api/init")
    public ResponseEntity getTestBlog() {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml"
        );
        Blog blog = context.getBean("blog", Blog.class);
        context.close();

        if (blog == null) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity(blog, HttpStatus.OK);
        }
    }


//    @GetMapping("/api/post")
////    public ResponseEntity getPost(int offset, int limit, String mode) {
//    public ResponseEntity getPost() {
//        Iterable<Post> allPosts = postRepository.findAll();
//        ArrayList<Post> posts = new ArrayList<>();
//        for (Post post : allPosts) {
//            posts.add(post);
//            List<PostVote> voteList = post.getPostVote();
//
//        }
//
//        int likeCount = 0;
//        int dislikeCOunt = 0;
//        int postCount = posts.size();
//
//
//
//        TreeMap<String, String> test = new TreeMap<>();
//        Integer id = 11;
//        test.put("id", id.toString());
//        test.put("name","Vasia");
//        TestClass new1 = new TestClass(12,test);
//
////        ArrayList<Post> ll = new ArrayList<>();
////        for (int i = 0; i < 10 ; i++) {
////                ll.add(i);
////        }
//
//        AllPosts allPosts1 = new AllPosts(posts.size(), posts);
//        return new ResponseEntity(allPosts1, HttpStatus.OK);
//
//    }


}
