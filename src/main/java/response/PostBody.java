package response;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostBody {

        private int id;
        private String time;
        private UserBody user;
        private String title;
        private String announce;
        private int likeCount;
        private int dislikeCount;
        private int commentCount;
        private int viewCount;
        private List<CommentBody> comments;
        private String[] tags;
}
