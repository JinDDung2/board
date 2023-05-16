package com.example.sns.entity.dto.post;

import com.example.sns.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {

    @NotNull
    private String title;
    @NotNull
    private String body;

    @Builder
    public PostCreateRequestDto(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .body(this.body)
                .likeCounts(0)
                .build();
    }
}
