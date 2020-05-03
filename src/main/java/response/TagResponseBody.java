package response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class TagResponseBody {
    private List<TagBody> tags;
}
