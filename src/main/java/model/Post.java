package model;

import lombok.Data;
import enums.ModerationStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "is_active")
    private byte isActive;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "moderation_status", columnDefinition = "moderation_status DEFAULT 'NEW'")
    private ModerationStatus moderationStatus;


    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @NotNull
    private LocalDateTime time;

    @NotNull
    private String title;

    @NotNull
    private String text;

    @NotNull
    @Column(name = "view_count")
    private int viewCount;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tag2post",
        joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private List<Tag> postTags;

    @OneToMany(mappedBy = "post")
    private List<PostVote> postVote;
}
