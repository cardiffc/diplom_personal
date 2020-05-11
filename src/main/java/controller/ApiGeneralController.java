package controller;

import main.SpringConfig;
import model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import response.CalendarResponseBody;
import response.TagResponseBody;
import services.AuthService;
import services.CalendarService;
import services.TagService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class ApiGeneralController {

    @Autowired
    private TagService tagService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private AuthService authService;

    @GetMapping("/api/init")
    public ResponseEntity getBlogInfo() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        Blog blog = context.getBean("blog", Blog.class);
        context.close();
        if (blog == null) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity(blog, HttpStatus.OK);
        }
    }


    @GetMapping("/api/tag")
    public ResponseEntity getTags(@RequestParam(required = false) String query)
    {
        TagResponseBody tagResponseBody;
        if (query == null)
            tagResponseBody = tagService.getAllTags();
        else
            tagResponseBody = tagService.getSearchedTags(query);
        return new ResponseEntity(tagResponseBody, HttpStatus.OK);
    }

    @GetMapping("/api/calendar")
    public ResponseEntity getCalendar(@RequestParam(required = false) String year) {
         if (year == null || !year.matches("[0-9]{4}") ||
                (Integer.parseInt(year) > 2020 || Integer.parseInt(year) < 2015))
            year = Integer.toString(LocalDateTime.now().getYear());
        CalendarResponseBody calendarResponseBody = calendarService.getCalendar(year);
        return new ResponseEntity(calendarResponseBody, HttpStatus.OK);
    }

    @GetMapping("/api/settings")
    public ResponseEntity settings() {
        return null;
    }



    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }


}
