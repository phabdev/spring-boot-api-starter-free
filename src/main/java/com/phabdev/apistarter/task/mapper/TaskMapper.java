package com.phabdev.apistarter.task.mapper;

import com.phabdev.apistarter.task.TaskItem;
import com.phabdev.apistarter.task.TaskPriority;
import com.phabdev.apistarter.task.TaskStatus;
import com.phabdev.apistarter.task.dto.CreateTaskRequest;
import com.phabdev.apistarter.task.dto.TaskResponse;
import com.phabdev.apistarter.task.dto.UpdateTaskRequest;
import org.springframework.stereotype.Component;

/**
 * Hand-written mapper between the {@link TaskItem} entity and its DTOs.
 * Small enough that a mapping library would add more noise than value.
 */
@Component
public class TaskMapper {

    public TaskItem toEntity(CreateTaskRequest request) {
        TaskPriority priority = request.priority() != null ? request.priority() : TaskPriority.MEDIUM;
        return new TaskItem(request.title(), request.description(), TaskStatus.TODO, priority);
    }

    public void applyUpdate(TaskItem task, UpdateTaskRequest request) {
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setPriority(request.priority());
    }

    public TaskResponse toResponse(TaskItem task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
