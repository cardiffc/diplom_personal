package main;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;
import java.util.Date;


@SpringBootApplication
@EntityScan(basePackages = {"model"})
@EnableJpaRepositories("repositories")
@ComponentScan(basePackages = {"controller"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        Date date = new Date();
        System.out.println(LocalDateTime.now());

    }


}
