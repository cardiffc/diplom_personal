package response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagBody {
    private String name;
    private double weight;
}
