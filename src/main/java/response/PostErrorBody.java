package response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostErrorBody {
    private String title;
    private String text;
}
