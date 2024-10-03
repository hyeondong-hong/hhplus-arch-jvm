package io.hhplus.arch.common.application.response;

public record ErrorResponse(
        String code,
        String message
) {
}
