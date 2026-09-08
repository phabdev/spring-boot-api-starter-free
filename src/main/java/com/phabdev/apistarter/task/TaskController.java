package com.phabdev.apistarter.task;

import com.phabdev.apistarter.common.error.ApiErrorResponse;
import com.phabdev.apistarter.common.web.ApiPaths;
import com.phabdev.apistarter.task.dto.CreateTaskRequest;
import com.phabdev.apistarter.task.dto.TaskResponse;
import com.phabdev.apistarter.task.dto.UpdateTaskRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(ApiPaths.TASKS)
@Tag(name = "Tasks", description = "CRUD operations on tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List all tasks")
    public List<TaskResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by id")
    @ApiResponse(responseCode = "200", description = "Task found")
    @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public TaskResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    @ApiResponse(responseCode = "201", description = "Task created")
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest request,
                                               UriComponentsBuilder uriBuilder) {
        TaskResponse created = service.create(request);
        URI location = uriBuilder.path(ApiPaths.TASKS + "/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace an existing task")
    @ApiResponse(responseCode = "200", description = "Task updated")
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a task")
    @ApiResponse(responseCode = "204", description = "Task deleted")
    @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
