package com.example.sns.entity.dto.comment;

import com.example.sns.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequestDto {
    private String comment;

    @Builder
    public CommentCreateRequestDto(String comment) {
        this.comment = comment;
    }

    public Comment toEntity() {
        return Comment.builder()
                .comment(comment)
                .build();
    }
}
