package main;


import model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import repositories.PostRepository;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
@EntityScan("model")
@EnableJpaRepositories("repositories")
@ComponentScan(basePackages = {"controller","main","services"})
public class Main {

//    @Autowired
//    private PostRepository postRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("Application started at " + LocalDateTime.now());


    }


}
