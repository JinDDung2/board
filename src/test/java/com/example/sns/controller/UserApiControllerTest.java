package com.example.sns.controller;

import com.example.sns.entity.Role;
import com.example.sns.entity.dto.user.UserJoinRequestDto;
import com.example.sns.entity.dto.user.UserJoinResponseDto;
import com.example.sns.entity.dto.user.UserLoginRequestDto;
import com.example.sns.entity.dto.user.UserLoginResponseDto;
import com.example.sns.exception.SpringBootAppException;
import com.example.sns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.sns.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser
    void 회원가입_성공() throws Exception {
        // given
        UserJoinRequestDto requestDto = new UserJoinRequestDto("testName", "testPwd");
        given(userService.join(any(UserJoinRequestDto.class)))
                .willReturn(new UserJoinResponseDto(100, requestDto.getUserName(), Role.USER));
        // when
        // then
        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.userId").value(100))
                .andExpect(jsonPath("$.result.userName").value("testName"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void 회원가입_실패_중복이름() throws Exception {
        UserJoinRequestDto requestDto = new UserJoinRequestDto("testName", "testPwd");

        given(userService.join(any(UserJoinRequestDto.class)))
                .willThrow(new SpringBootAppException(DUPLICATED_USER_NAME, requestDto.getUserName() + " 아이디가 중복입니다."));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(print());

    }

    @Test
    @WithMockUser
    void 로그인_성공() throws Exception {
        UserLoginRequestDto requestDto = new UserLoginRequestDto("testName", "testPwd");

        given(userService.login(any(UserLoginRequestDto.class)))
                .willReturn(new UserLoginResponseDto("token"));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.jwt").value("token"))
                .andDo(print());

    }

    @Test
    @WithMockUser
    void 로그인_실패_아이디_없음() throws Exception {
        UserLoginRequestDto requestDto = new UserLoginRequestDto("testName", "testPwd");

        given(userService.login(any(UserLoginRequestDto.class)))
                .willThrow(new SpringBootAppException(USERNAME_NOT_FOUND, requestDto.getUserName() + " 아이디를 찾을 수 없습니다."));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @WithMockUser
    void 로그인_실패_패스워드_다름() throws Exception {
        UserLoginRequestDto requestDto = new UserLoginRequestDto("testName", "testPwd");

        given(userService.login(any(UserLoginRequestDto.class)))
                .willThrow(new SpringBootAppException(INVALID_PASSWORD, "패스워드가 다릅니다."));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }

    @Test
    @WithMockUser
    void 알람_목록_조회_성공() throws Exception {

        given(userService.findAlarm(any(), any())).willReturn(Page.empty());

        mockMvc.perform(get("/api/v1/alarms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void 알람_목록_조회_비로그인_실패() throws Exception {

        given(userService.findAlarm(any(), any())).willReturn(Page.empty());
        when(userService.findAlarm(any(), any()))
                .thenThrow(new SpringBootAppException(INVALID_PERMISSION, "사용자가 권한이 없습니다."));


        mockMvc.perform(get("/api/v1/alarms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

}