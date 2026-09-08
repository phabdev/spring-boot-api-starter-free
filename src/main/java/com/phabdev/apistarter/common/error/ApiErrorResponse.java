package com.phabdev.apistarter.common.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Standard error payload returned by every failing endpoint.
 */
@Schema(description = "Standard error response")
public record ApiErrorResponse(
        @Schema(example = "2026-09-09T18:00:00Z") OffsetDateTime timestamp,
        @Schema(example = "400") int status,
        @Schema(example = "Bad Request") String error,
        @Schema(example = "Validation failed") String message,
        @Schema(example = "/api/tasks") String path,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<FieldError> fieldErrors
) {

    @Schema(description = "Validation error on a single field")
    public record FieldError(
            @Schema(example = "title") String field,
            @Schema(example = "must not be blank") String message
    ) {
    }
}
