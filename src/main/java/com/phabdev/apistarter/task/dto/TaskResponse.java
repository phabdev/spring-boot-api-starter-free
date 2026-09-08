package com.phabdev.apistarter.task.dto;

import com.phabdev.apistarter.task.TaskPriority;
import com.phabdev.apistarter.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Task as returned by the API")
public record TaskResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Build the free starter kit") String title,
        @Schema(example = "Create the public open-core version") String description,
        @Schema(example = "TODO") TaskStatus status,
        @Schema(example = "HIGH") TaskPriority priority,
        @Schema(example = "2026-09-09T18:00:00Z") OffsetDateTime createdAt,
        @Schema(example = "2026-09-09T18:00:00Z") OffsetDateTime updatedAt
) {
}
