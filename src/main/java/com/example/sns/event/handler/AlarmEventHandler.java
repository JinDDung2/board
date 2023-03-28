package com.example.sns.event.handler;

import com.example.sns.event.AlarmEvent;
import com.example.sns.repository.AlarmRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AlarmEventHandler {
    private final AlarmRepository alarmRepository;

    public AlarmEventHandler(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @EventListener
    public void createAlarm(AlarmEvent event) {
        alarmRepository.save(event.getAlarm());
    }
}
