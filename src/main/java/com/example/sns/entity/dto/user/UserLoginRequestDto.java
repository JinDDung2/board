package com.example.sns.entity.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserLoginRequestDto {

    @NotBlank
    private String userName;
    @NotBlank
    private String password;

    @Builder
    public UserLoginRequestDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
