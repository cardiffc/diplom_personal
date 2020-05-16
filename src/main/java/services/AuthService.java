package services;

import lombok.Data;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import repositories.UserRepository;
import response.AuthResponseBody;
import response.UserBody;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Data
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private ConcurrentHashMap<String, Integer> authorizedSessions = new ConcurrentHashMap<>();

    public AuthResponseBody logonUser(String email, String password) {
        User currentUser = userRepository.findByEmail(email);
        if (currentUser == null || !currentUser.getPassword().equals(password)) {
            return AuthResponseBody.builder().result(false).build();
        } else {
            authorizedSessions.put(getSession().getId(), currentUser.getId());
            return generateSuccessResponse(currentUser);
        }
    }

    public AuthResponseBody checkAuth() {
        String key = getSession().getId();
        Integer userId;
        if (authorizedSessions.containsKey(key)) {
            userId = authorizedSessions.get(key);
            Query currentUserQuery = entityManager.createQuery("" +
                    "from User u where u.id = :id", User.class).setParameter("id", userId);
            User curruntUser = (User) currentUserQuery.getResultList().get(0);
            return generateSuccessResponse(curruntUser);
        } else {
            return AuthResponseBody.builder().result(false).build();
        }

    }

    public AuthResponseBody logout() {
        String key = getSession().getId();
        if (authorizedSessions.containsKey(key)) {
            authorizedSessions.remove(key);
        }
        return AuthResponseBody.builder().result(true).build();

    }

    private AuthResponseBody generateSuccessResponse(User user) {
        boolean moderator = false;
        boolean settings = false;
        long moderationsCount = 0;
        if (user.getIsModerator() == 1) {
            moderator = true;
            settings = true;
            Query query = entityManager.createQuery("select count(*) from Post p where p.moderationStatus = 'NEW' " +
                    "and p.moderatorId = :moderator").setParameter("moderator", user.getId());
            moderationsCount = (long) query.getSingleResult();
        }
        UserBody userBody = UserBody.builder().id(user.getId()).name(user.getName()).photo(user.getPhoto())
                .email(user.getEmail()).moderation(moderator).moderationCount(moderationsCount).settings(settings).build();
        AuthResponseBody authResponseBody =  AuthResponseBody.builder().result(true).user(userBody).build();
        return authResponseBody;
    }

    public HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }

    public boolean isUserAuthorized() {
       String sessionId = getSession().getId();
       return authorizedSessions.containsKey(sessionId);
    }

    public int getLoggedUserId() {
        return authorizedSessions.get(getSession().getId());

    }




}
