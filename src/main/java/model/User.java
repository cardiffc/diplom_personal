package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "is_moderated")
    private byte isModerator;

    @NotNull
    @Column(name = "reg_time")
    private LocalDateTime regTime;

    @NotNull
    private String name;

    @NotNull
    @JsonIgnore
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String code;

    @NotNull
    private String photo;
}
