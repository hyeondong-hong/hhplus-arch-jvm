package io.hhplus.arch.application.lecture.dto;

import org.springframework.lang.NonNull;

import java.time.LocalDate;

public record LectureScheduleDTO(
        @NonNull Long id,
        @NonNull LocalDate lectureDate,
        @NonNull Integer capacity
) {
}
