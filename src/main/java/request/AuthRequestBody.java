package request;

import lombok.Data;

@Data
public class AuthRequestBody {
    private String e_mail;
    private String password;
    private String test;


}
