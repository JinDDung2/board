package com.example.sns.entity.dto;

import com.example.sns.entity.Alarm;
import com.example.sns.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AlarmReadResponse {

    private Integer id;
    private Alarm.AlarmType alarmType;
    private User fromUser;
    private User targetUser;
    private String text;
    private LocalDateTime createdAt;

    @Builder
    public AlarmReadResponse(Integer id, Alarm.AlarmType alarmType, User fromUser, User targetUser, String text, LocalDateTime createdAt) {
        this.id = id;
        this.alarmType = alarmType;
        this.fromUser = fromUser;
        this.targetUser = targetUser;
        this.text = text;
        this.createdAt = createdAt;
    }

    public static AlarmReadResponse from(Alarm alarm) {
        return AlarmReadResponse.builder()
                .id(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .fromUser(alarm.getFromUser())
                .targetUser(alarm.getTargetUser())
                .text(alarm.getAlarmType().getMessage())
                .build();
    }
}
