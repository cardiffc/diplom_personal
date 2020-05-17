package model;

import enums.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import enums.ModerationStatus;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "posts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "is_active")
    private byte isActive;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "moderation_status")
    private ModerationStatus moderationStatus;


    @Column(name = "moderator_id")
    private Integer moderatorId;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   // @Column(name = "user_id")
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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "tag2post",
        joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private List<Tag> postTags;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostVote> postVote;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostComment> postComments;

    public int getVotes(VoteType value) {
        int likes = 0;
        int disLikes = 0;
        List<PostVote> votes = getPostVote();
        for (PostVote vote : votes) {
            if (vote.getValue() == 1) {
                likes++;
            } else {
                disLikes++;
            }
        }
        return (value.equals(VoteType.like)) ? likes :disLikes;
    }

    public int getCommentsCount() {
        return getPostComments().size();
    }
}
