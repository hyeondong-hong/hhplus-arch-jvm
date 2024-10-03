package io.hhplus.arch.application.lecture.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LectureEnrollRequest(
        @NotNull @Min(1) Long lectureScheduleId,
        @NotNull @Min(1) Long userId
) {
}
