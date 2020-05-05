package request;

import lombok.Data;

@Data
public class LoginBody {
    private String e_mail;
    private String password;

}
