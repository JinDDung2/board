package com.example.sns.entity.dto.post;

import com.example.sns.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUpdateResponseDto {

    private Integer postId;
    private String message;

    @Builder
    public PostUpdateResponseDto(Integer postId, String message) {
        this.postId = postId;
        this.message = message;
    }

    public static PostUpdateResponseDto from(Post post) {
        return PostUpdateResponseDto.builder()
                .postId(post.getId())
                .message("포스트 수정 완료")
                .build();
    }
}
