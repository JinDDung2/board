package com.example.sns.event;

import com.example.sns.entity.Alarm;
import com.example.sns.entity.User;
import lombok.Getter;

@Getter
public class AlarmEvent {
    private final Alarm alarm;

    public AlarmEvent(Alarm alarm) {
        this.alarm = alarm;
    }

    public static AlarmEvent from(Alarm.AlarmType alarmType, User target, User from) {
        return new AlarmEvent(Alarm.builder()
                .alarmType(alarmType)
                .targetUser(target)
                .fromUser(from)
                .build());
    }
}
