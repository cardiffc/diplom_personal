package services;

import org.springframework.stereotype.Component;
import response.CalendarResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Component
public class CalendarService {
    @PersistenceContext
    EntityManager entityManager;

    public CalendarResponseBody getCalendar(String year) {

        Query yearsQuery = entityManager.createQuery("select year(time) as years from Post p where " +
                                        "p.moderationStatus = 'ACCEPTED' and p.isActive = '1' group by years");

        List<Integer> years = yearsQuery.getResultList();
        Collections.sort(years, Collections.reverseOrder());

        Query currentYearPostsQuery = entityManager.createQuery("select count(*) as posts_count, to_char(time,'YYYY-MM-DD') as day " +
                "from Post p where p.moderationStatus = 'ACCEPTED' and p.isActive = '1' and to_char(time,'YYYY') = :year " +
                "group by day");
        currentYearPostsQuery.setParameter("year", year);
        List<Object[]> currentYearPosts = currentYearPostsQuery.getResultList();
        HashMap<String, Long> postsMap = new HashMap<>();
        currentYearPosts.forEach(currentYearPost -> {
            String day = currentYearPost[1].toString();
            Long count = (Long) currentYearPost[0];
            postsMap.put(day,count);
        });
        return CalendarResponseBody.builder().years(years).posts(postsMap).build();
    }
}
