package io.hhplus.arch.application.lecture.dto;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record LectureEnrollDTO(
        @NonNull Long id,
        @NonNull Long userId,
        @Nullable EnrolledLectureScheduleDTO schedule
) {
}
