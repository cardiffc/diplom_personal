package model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
@Entity
@Table(name = "post_votes")
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    private Post post;

    @NotNull
    private Date time;

    @NotNull
    private byte value;
}
