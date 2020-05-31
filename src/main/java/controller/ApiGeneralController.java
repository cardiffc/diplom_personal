package controller;

import main.SpringConfig;
import model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import repositories.UserRepository;
import request.ModerateRequestBody;
import response.AuthResponseBody;
import response.CalendarResponseBody;
import response.StatiscticsResponse;
import response.TagResponseBody;
import services.*;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
public class ApiGeneralController {

    @Autowired
    private TagService tagService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeneralService generalService;

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

    @GetMapping("/api/statistics/my")
    public ResponseEntity getMyStatistics () {
            if (!authService.isUserAuthorized())
                return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
            int userId = authService.getLoggedUserId();
        StatiscticsResponse statiscticsResponse = generalService.getMyStatistics(userId);
            return new ResponseEntity(statiscticsResponse, HttpStatus.OK);
    }

    @GetMapping("/api/statistics/all")
    public ResponseEntity getAllStatistics () {
        if (!authService.isUserAuthorized())
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        StatiscticsResponse statiscticsResponse = generalService.getAllStatistics();
        return new ResponseEntity(statiscticsResponse, HttpStatus.OK);
    }

    @PostMapping("/api/moderation")
    public ResponseEntity moderatePost(@RequestBody ModerateRequestBody body) {
        if (!authService.isUserAuthorized())
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        int userId = authService.getLoggedUserId();
        if (userRepository.findById(userId).getIsModerator() == 0)
            return new ResponseEntity("User not moderator", HttpStatus.BAD_REQUEST);
        AuthResponseBody authResponseBody = postService.moderatePost(body.getPost_id(), body.getDecision(), userId);
        return new ResponseEntity(authResponseBody, HttpStatus.OK);

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
