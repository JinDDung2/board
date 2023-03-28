package com.example.sns.entity.dto.user;

import com.example.sns.entity.Role;
import com.example.sns.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserJoinResponseDto {

    private Integer userId;
    private String userName;
    private Role userRole;

    @Builder
    public UserJoinResponseDto(Integer userId, String userName, Role userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
    }

    public static UserJoinResponseDto from(User user) {
        return UserJoinResponseDto.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .userRole(user.getRole())
                .build();
    }
}
