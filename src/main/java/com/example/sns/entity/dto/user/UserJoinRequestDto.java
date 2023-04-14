package com.example.sns.entity.dto.user;

import com.example.sns.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static com.example.sns.entity.Role.USER;


@Getter
@NoArgsConstructor
public class UserJoinRequestDto {

    @NotBlank
    private String userName;
    @NotBlank
    private String password;

    @Builder
    public UserJoinRequestDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User toEntity(String encodedPassword) {
        return User.builder()
                .userName(this.userName)
                .password(encodedPassword)
                .role(USER)
                .build();
    }
}
