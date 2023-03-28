package com.example.sns.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "alarms")
@Where(clause = "deleted_date IS NULL")
public class Alarm extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Integer id;

    @ManyToOne
    private User fromUser;
    @ManyToOne
    private User targetUser;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Builder
    public Alarm(User fromUser, User targetUser, AlarmType alarmType) {
        this.fromUser = fromUser;
        this.targetUser = targetUser;
        this.alarmType = alarmType;
    }

    @Getter
    public enum AlarmType {
        NEW_COMMENT_ON_POST("new comment!"),
        NEW_LIKE_ON_POST("new like!");

        AlarmType(String message) {
            this.message = message;
        }

        private String message;
    }
}
