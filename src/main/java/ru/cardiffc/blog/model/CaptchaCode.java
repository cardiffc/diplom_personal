package ru.cardiffc.blog.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private LocalDateTime time;

    @NotNull
    private String code;

    @NotNull
    @Column(name = "secret_code")
    private String secretCode;
}
