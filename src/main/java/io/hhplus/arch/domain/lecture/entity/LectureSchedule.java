package io.hhplus.arch.domain.lecture.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "`lecture_schedule`", indexes = {@Index(columnList = "lecture_id")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"lecture_id", "lecture_date"})})
@EntityListeners(AuditingEntityListener.class)
public class LectureSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lectureId;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private LocalDate lectureDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

}
