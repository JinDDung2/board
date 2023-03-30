package com.example.sns.event.handler;

import com.example.sns.event.AlarmEvent;
import com.example.sns.repository.AlarmRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AlarmEventHandler {
    private final AlarmRepository alarmRepository;

    public AlarmEventHandler(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void createAlarm(AlarmEvent event) {
        alarmRepository.save(event.getAlarm());
    }
}

