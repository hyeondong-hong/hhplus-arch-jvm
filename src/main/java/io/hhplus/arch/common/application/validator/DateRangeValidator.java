package io.hhplus.arch.common.application.validator;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateRangeValidator {

    @NonNull
    public LocalDate init(@Nullable LocalDate date) {
        if (date == null) {
            return LocalDate.now();
        }
        return date;
    }

    public void validate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("종료일이 시작일 보다 빠름");
        }
    }
}
