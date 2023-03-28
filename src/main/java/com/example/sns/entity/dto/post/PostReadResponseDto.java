package com.example.sns.entity.dto.post;

import com.example.sns.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostReadResponseDto {

    private Integer id;
    private String title;
    private String body;
    private String userName;
    private Integer postLikes;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    @Builder
    public PostReadResponseDto(Integer id, String title, String body, String userName, Integer postLikes, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.userName = userName;
        this.postLikes = postLikes;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static PostReadResponseDto from (Post post) {
        return PostReadResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUser().getUserName())
                .postLikes(post.getLikeCounts())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .build();
    }
}
