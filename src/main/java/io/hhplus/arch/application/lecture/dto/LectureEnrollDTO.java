package io.hhplus.arch.application.lecture.dto;

import io.hhplus.arch.domain.lecture.entity.Lecture;
import io.hhplus.arch.domain.lecture.entity.LectureEnroll;
import io.hhplus.arch.domain.lecture.entity.LectureSchedule;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record LectureEnrollDTO(
        @NonNull Long id,
        @NonNull Long userId,
        @Nullable EnrolledLectureScheduleDTO schedule
) {
    public static LectureEnrollDTO of(
            @NonNull LectureEnroll enroll
    ) {
        return new LectureEnrollDTO(
                enroll.getId(),
                enroll.getUserId(),
                null
        );
    }

    public static LectureEnrollDTO of(
            @NonNull LectureEnroll enroll,
            @NonNull LectureSchedule schedule,
            @NonNull Lecture lecture
    ) {
        return new LectureEnrollDTO(
                enroll.getId(),
                enroll.getUserId(),
                new EnrolledLectureScheduleDTO(
                        schedule.getId(),
                        schedule.getLectureDate(),
                        new EnrolledLectureDTO(
                                lecture.getId(),
                                lecture.getTitle(),
                                lecture.getLecturerName()
                        )
                )
        );
    }
}
