package com.phabdev.apistarter.task.dto;

import com.phabdev.apistarter.task.TaskPriority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload used to create a new task")
public record CreateTaskRequest(

        @Schema(description = "Short title of the task", example = "Build the free starter kit", maxLength = 120)
        @NotBlank
        @Size(max = 120)
        String title,

        @Schema(description = "Optional longer description", example = "Create the public open-core version", maxLength = 1000)
        @Size(max = 1000)
        String description,

        @Schema(description = "Priority. Defaults to MEDIUM when omitted", example = "HIGH")
        TaskPriority priority
) {
}
