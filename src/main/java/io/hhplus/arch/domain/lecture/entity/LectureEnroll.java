package io.hhplus.arch.domain.lecture.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`lecture_enroll`", uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"user_id", "lecture_id"})
        @UniqueConstraint(columnNames = {"user_id", "lecture_schedule_id"})
})
public class LectureEnroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long lectureScheduleId;

    @CreatedDate
    private LocalDateTime createdAt;

    public static LectureEnroll of(Long userId, Long lectureScheduleId) {
        LectureEnroll enroll = new LectureEnroll();
        enroll.setUserId(userId);
        enroll.setLectureScheduleId(lectureScheduleId);
        return enroll;
    }
}
