package com.phabdev.apistarter.task;

import com.phabdev.apistarter.common.error.ResourceNotFoundException;
import com.phabdev.apistarter.task.dto.CreateTaskRequest;
import com.phabdev.apistarter.task.dto.TaskResponse;
import com.phabdev.apistarter.task.dto.UpdateTaskRequest;
import com.phabdev.apistarter.task.mapper.TaskMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskService(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> findAll() {
        return repository.findAll(Sort.by("id"))
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    public TaskResponse create(CreateTaskRequest request) {
        TaskItem saved = repository.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    public TaskResponse update(Long id, UpdateTaskRequest request) {
        TaskItem task = getOrThrow(id);
        mapper.applyUpdate(task, request);
        return mapper.toResponse(repository.save(task));
    }

    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private TaskItem getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }
}
