package repositories;
import enums.ModerationStatus;
import model.Post;
import model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
//import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer > {

    List<Post> findPostByModerationStatusAndIsActiveAndTimeBeforeAndUser(ModerationStatus status, byte isActive,
                                                                         LocalDateTime time, User user);

    List<Post> findPostByModerationStatusAndIsActiveAndTimeBefore(ModerationStatus status,
                                                                  byte isActive, LocalDateTime time);

    Post findPostByIdAndIsActiveAndModerationStatus(int id, byte isActive, ModerationStatus status);

    List<Post> findPostByModerationStatusAndIsActive(ModerationStatus status, byte isActive, Pageable pageable);

//    from Post p where p.id = :postId and p.isActive = '1' " +
//            "and p.moderationStatus = 'ACCEPTED'"

}