package com.example.sns.entity.dto.user;

import com.example.sns.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.example.sns.entity.Role.USER;


@Getter
@NoArgsConstructor
public class UserJoinRequestDto {

    @NotBlank
    @Pattern(regexp="[a-zA-Z1-9]{4,12}", message = "아이디는 영어와 숫자로 포함해서 4~12자리 이내로 입력해주세요.")
    private String userName;
    @NotBlank
    @Pattern(regexp="[a-zA-Z1-9]{6,12}", message = "비밀번호는 영어와 숫자로 포함해서 6~12자리 이내로 입력해주세요.")
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
