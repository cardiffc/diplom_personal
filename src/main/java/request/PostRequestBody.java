package request;
import lombok.Data;
import java.util.List;

@Data
public class PostRequestBody {
    private String time;
    private int active;
    private String title;
    private String text;
    List<String> tags;
}
