package response;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class CalendarResponseBody {

    private List<Integer> years;
    private HashMap<String, Long> posts;
}
