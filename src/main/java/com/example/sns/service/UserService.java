package com.example.sns.service;

import com.example.sns.entity.Alarm;
import com.example.sns.entity.User;
import com.example.sns.entity.dto.*;
import com.example.sns.entity.dto.user.*;
import com.example.sns.exception.SpringBootAppException;
import com.example.sns.jwt.JwtTokenUtils;
import com.example.sns.repository.AlarmRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.sns.entity.Role.ADMIN;
import static com.example.sns.entity.Role.USER;
import static com.example.sns.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AlarmRepository alarmRepository;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expiredTimeMs = 1000 * 60 * 60;

    @Transactional
    public UserJoinResponseDto join(UserJoinRequestDto requestDto) {

        if (userRepository.existsByUserName(requestDto.getUserName())) {
            throw new SpringBootAppException(DUPLICATED_USER_NAME, requestDto.getUserName() + "이 중복됩니다");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = requestDto.toEntity(encodedPassword);
        userRepository.save(user);

        return UserJoinResponseDto.from(user);
    }

    @Transactional(readOnly = true)
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {

        User findUser = userRepository.findByUserName(requestDto.getUserName()).orElseThrow(() -> {
            throw new SpringBootAppException(USERNAME_NOT_FOUND, requestDto.getUserName() + " 아이디가 존재하지 않습니다.");
        });

        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new SpringBootAppException(INVALID_PASSWORD, "비밀번호가 다릅니다.");
        }

        String token = jwtTokenUtils.createToken(requestDto.getUserName(), secretKey, expiredTimeMs);
        return new UserLoginResponseDto(token);
    }

    @Transactional
    public UserRoleResponseDto changeRole(Integer changeId, String userName) {
        User admin = userRepository.findByUserName(userName).orElseThrow(() -> {
            throw new SpringBootAppException(USERNAME_NOT_FOUND, "admin 유저가 존재하지 않습니다.");
        });

        User changeUser = userRepository.findById(changeId).orElseThrow(() -> {
            throw new SpringBootAppException(USERNAME_NOT_FOUND, "유저가 존재하지 않습니다.");
        });

        if (admin.getRole() != ADMIN) {
            throw new SpringBootAppException(ROLE_FORBIDDEN, "접근 권한이 없습니다.");
        }

        if (changeUser.getRole() == USER) changeUser.upgradeAdmin(changeUser);
        else if (changeUser.getRole() == ADMIN) changeUser.downgradeUser(changeUser);

        return UserRoleResponseDto.from(changeUser);
    }

    @Transactional(readOnly = true)
    public Page<AlarmReadResponse> findAlarm(Pageable pageable, String userName) {

        User findUser = userRepository.findByUserName(userName).orElseThrow(() -> {
            throw new SpringBootAppException(USERNAME_NOT_FOUND, userName + " 아이디가 존재하지 않습니다.");
        });

        Page<Alarm> pages = alarmRepository.findAllByUserId(findUser.getId(), pageable);
        return pages.map(AlarmReadResponse::from);
    }
}
