package io.hhplus.arch.application.lecture.dto;

import org.springframework.lang.NonNull;

import java.time.LocalDate;

public record EnrolledLectureScheduleDTO(
        @NonNull Long id,
        @NonNull LocalDate lectureDate,
        @NonNull EnrolledLectureDTO lecture
) {
}
