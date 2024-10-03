package io.hhplus.arch.common.application.dto;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

public record DateRangeDTO(
        @NonNull LocalDate startDate,
        @NonNull LocalDate endDate
) {
    public DateRangeDTO(
            @Nullable LocalDate startDate,
            @Nullable LocalDate endDate
    ) {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
