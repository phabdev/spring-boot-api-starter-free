package com.phabdev.apistarter.task;

import com.phabdev.apistarter.common.error.ResourceNotFoundException;
import com.phabdev.apistarter.task.dto.CreateTaskRequest;
import com.phabdev.apistarter.task.dto.TaskResponse;
import com.phabdev.apistarter.task.dto.UpdateTaskRequest;
import com.phabdev.apistarter.task.mapper.TaskMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    private TaskService service;

    @BeforeEach
    void setUp() {
        service = new TaskService(repository, new TaskMapper());
    }

    @Test
    void createUsesTodoStatusAndDefaultsPriorityToMedium() {
        when(repository.save(any(TaskItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = service.create(new CreateTaskRequest("Write docs", null, null));

        assertThat(response.title()).isEqualTo("Write docs");
        assertThat(response.status()).isEqualTo(TaskStatus.TODO);
        assertThat(response.priority()).isEqualTo(TaskPriority.MEDIUM);
    }

    @Test
    void createKeepsExplicitPriority() {
        when(repository.save(any(TaskItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = service.create(new CreateTaskRequest("Urgent fix", "Prod is down", TaskPriority.HIGH));

        assertThat(response.priority()).isEqualTo(TaskPriority.HIGH);
        assertThat(response.description()).isEqualTo("Prod is down");
    }

    @Test
    void findByIdReturnsTaskWhenPresent() {
        TaskItem task = new TaskItem("Existing", null, TaskStatus.IN_PROGRESS, TaskPriority.LOW);
        when(repository.findById(7L)).thenReturn(Optional.of(task));

        TaskResponse response = service.findById(7L);

        assertThat(response.title()).isEqualTo("Existing");
        assertThat(response.status()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(42L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task with id 42 not found");
    }

    @Test
    void updateAppliesAllFields() {
        TaskItem task = new TaskItem("Old title", "Old description", TaskStatus.TODO, TaskPriority.LOW);
        when(repository.findById(1L)).thenReturn(Optional.of(task));
        when(repository.save(any(TaskItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = service.update(1L,
                new UpdateTaskRequest("New title", "New description", TaskStatus.DONE, TaskPriority.HIGH));

        ArgumentCaptor<TaskItem> captor = ArgumentCaptor.forClass(TaskItem.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue()).isSameAs(task);
        assertThat(response.title()).isEqualTo("New title");
        assertThat(response.description()).isEqualTo("New description");
        assertThat(response.status()).isEqualTo(TaskStatus.DONE);
        assertThat(response.priority()).isEqualTo(TaskPriority.HIGH);
    }

    @Test
    void deleteRemovesExistingTask() {
        TaskItem task = new TaskItem("To delete", null, TaskStatus.TODO, TaskPriority.MEDIUM);
        when(repository.findById(3L)).thenReturn(Optional.of(task));

        service.delete(3L);

        verify(repository).delete(task);
    }

    @Test
    void deleteThrowsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L)).isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).delete(any(TaskItem.class));
    }
}
