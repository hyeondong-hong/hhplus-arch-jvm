package io.hhplus.arch.application.lecture.dto;

import org.springframework.lang.NonNull;

import java.util.List;

public record LectureDTO(
        @NonNull Long id,
        @NonNull String title,
        @NonNull String lecturerName,
        @NonNull List<LectureScheduleDTO> schedules
) {
}
