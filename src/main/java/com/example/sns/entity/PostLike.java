package com.example.sns.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_likes")
@Where(clause = "deleted_date IS NULL")
public class PostLike extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Integer id;
    private boolean isLike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    public static PostLike from(Post post, User user) {
        return PostLike.builder()
                .post(post)
                .user(user)
                .build();
    }

    public boolean likes() {
        if (isLike) post.decreaseLike();
        else post.increaseLike();
        clickLike();
        return isLike;
    }

    private void clickLike() {
        isLike = !isLike;
    }
}
