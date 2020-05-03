package response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentBody {
    private int id;
    private LocalDateTime time;
    private String text;
    private UserBody user;

}
