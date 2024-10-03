package io.hhplus.arch.application.lecture.dto;

import org.springframework.lang.NonNull;

public record EnrolledLectureDTO(
        @NonNull Long id,
        @NonNull String title,
        @NonNull String lecturerName
) {
}
