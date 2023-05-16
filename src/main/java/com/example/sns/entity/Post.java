package com.example.sns.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "posts")
@Where(clause = "deleted_date IS NULL")
public class Post extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer id;

    @NotNull
    private String title;
    @NotNull
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostLike> postLikes = new ArrayList<>();

    private Integer likeCounts;

    @Version
    private Long version;

    @Builder
    public Post(Integer id, String title, String body, User user, Integer likeCounts, Long version) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.user = user;
        this.likeCounts = likeCounts;
        this.version = version;
    }
    // 연관관계 메서드
    public void addUser(User user) {
        this.user = user;
    }

    // 비지니스 로직
    public void update(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public synchronized void increaseLike() {
        likeCounts++;
    }

    public synchronized void decreaseLike() {
        if (likeCounts > 0) likeCounts--;
    }

    public boolean isEqualUser(User user) {
        return this.user.getId().equals(user.getId());
    }
}
