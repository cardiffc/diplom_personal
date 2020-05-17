package response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class AuthUserBody {
    private int id;
    private String name;
    private String photo;
    private String email;
    private boolean moderation;
    private Long moderationCount;
    private boolean settings;
}
