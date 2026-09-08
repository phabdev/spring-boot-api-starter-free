package com.phabdev.apistarter.task.dto;

import com.phabdev.apistarter.task.TaskPriority;
import com.phabdev.apistarter.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload used to fully replace an existing task")
public record UpdateTaskRequest(

        @Schema(description = "Short title of the task", example = "Build the free starter kit", maxLength = 120)
        @NotBlank
        @Size(max = 120)
        String title,

        @Schema(description = "Optional longer description", example = "Create the public open-core version", maxLength = 1000)
        @Size(max = 1000)
        String description,

        @Schema(description = "Current status", example = "IN_PROGRESS")
        @NotNull
        TaskStatus status,

        @Schema(description = "Priority", example = "HIGH")
        @NotNull
        TaskPriority priority
) {
}
