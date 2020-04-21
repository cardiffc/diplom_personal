package controller;

import main.SpringConfig;
import model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import repositories.PostRepository;

@Controller
public class ApiGeneralController {

    @GetMapping("/api/init")
    public ResponseEntity getTestBlog() {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        Blog blog = context.getBean("blog", Blog.class);
        context.close();

        if (blog == null) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity(blog, HttpStatus.OK);
        }
    }


    @GetMapping(value = "/api/tag")
    public ResponseEntity getTags(String query)
    {
        
        return null;
    }

}
