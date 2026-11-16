package com.taskmanager.taskmanager.service;

import com.taskmanager.taskmanager.dto.TaskRequestDTO;
import com.taskmanager.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.taskmanager.model.Priority;
import com.taskmanager.taskmanager.model.Status;
import com.taskmanager.taskmanager.model.Task;
import com.taskmanager.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;
    private TaskRequestDTO sampleRequest;
    private TaskResponseDTO sampleResponse;

    @BeforeEach
    void setUp() {
        sampleTask = Task.builder()
                .id(1L)
                .title("Rédiger le rapport")
                .description("Rapport mensuel")
                .dueDate(LocalDate.of(2025, 12, 31))
                .priority(Priority.HIGH)
                .status(Status.TODO)
                .createdAt(LocalDateTime.now())
                .build();

        sampleRequest = TaskRequestDTO.builder()
                .title("Rédiger le rapport")
                .description("Rapport mensuel")
                .dueDate(LocalDate.of(2025, 12, 31))
                .priority(Priority.HIGH)
                .status(Status.TODO)
                .build();

        sampleResponse = TaskResponseDTO.builder()
                .id(1L)
                .title("Rédiger le rapport")
                .description("Rapport mensuel")
                .dueDate(LocalDate.of(2025, 12, 31))
                .priority(Priority.HIGH)
                .status(Status.TODO)
                .createdAt(sampleTask.getCreatedAt())
                .build();
    }

    // ── Création ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("createTask — doit retourner la tâche créée")
    void createTask_success() {
        when(taskMapper.toEntity(sampleRequest)).thenReturn(sampleTask);
        when(taskRepository.save(sampleTask)).thenReturn(sampleTask);
        when(taskMapper.toDTO(sampleTask)).thenReturn(sampleResponse);

        TaskResponseDTO result = taskService.createTask(sampleRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Rédiger le rapport");
        verify(taskRepository, times(1)).save(sampleTask);
    }

    // ── Lecture de toutes les tâches ─────────────────────────────────────────

    @Test
    @DisplayName("getAllTasks — doit retourner la liste complète")
    void getAllTasks_returnsList() {
        when(taskRepository.findAll()).thenReturn(List.of(sampleTask));
        when(taskMapper.toDTO(sampleTask)).thenReturn(sampleResponse);

        List<TaskResponseDTO> result = taskService.getAllTasks();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Rédiger le rapport");
    }

    @Test
    @DisplayName("getAllTasks — doit retourner une liste vide si aucune tâche")
    void getAllTasks_empty() {
        when(taskRepository.findAll()).thenReturn(List.of());

        List<TaskResponseDTO> result = taskService.getAllTasks();

        assertThat(result).isEmpty();
    }

    // ── Lecture par ID ───────────────────────────────────────────────────────

    @Test
    @DisplayName("getTaskById — doit retourner la tâche pour un ID valide")
    void getTaskById_found() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskMapper.toDTO(sampleTask)).thenReturn(sampleResponse);

        TaskResponseDTO result = taskService.getTaskById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getTaskById — doit lever TaskNotFoundException pour un ID inexistant")
    void getTaskById_notFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(99L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── Modification ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("updateTask — doit mettre à jour et retourner la tâche modifiée")
    void updateTask_success() {
        TaskRequestDTO updateRequest = TaskRequestDTO.builder()
                .title("Rapport final")
                .description("Version finale")
                .priority(Priority.MEDIUM)
                .status(Status.IN_PROGRESS)
                .build();

        Task updatedTask = Task.builder()
                .id(1L)
                .title("Rapport final")
                .description("Version finale")
                .priority(Priority.MEDIUM)
                .status(Status.IN_PROGRESS)
                .createdAt(sampleTask.getCreatedAt())
                .build();

        TaskResponseDTO updatedResponse = TaskResponseDTO.builder()
                .id(1L)
                .title("Rapport final")
                .status(Status.IN_PROGRESS)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDTO(updatedTask)).thenReturn(updatedResponse);

        TaskResponseDTO result = taskService.updateTask(1L, updateRequest);

        assertThat(result.getTitle()).isEqualTo("Rapport final");
        assertThat(result.getStatus()).isEqualTo(Status.IN_PROGRESS);
    }

    @Test
    @DisplayName("updateTask — doit lever TaskNotFoundException si ID inexistant")
    void updateTask_notFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(99L, sampleRequest))
                .isInstanceOf(TaskNotFoundException.class);
    }

    // ── Suppression ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("deleteTask — doit supprimer la tâche existante")
    void deleteTask_success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        doNothing().when(taskRepository).delete(sampleTask);

        assertThatCode(() -> taskService.deleteTask(1L)).doesNotThrowAnyException();
        verify(taskRepository, times(1)).delete(sampleTask);
    }

    @Test
    @DisplayName("deleteTask — doit lever TaskNotFoundException si ID inexistant")
    void deleteTask_notFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(99L))
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository, never()).delete(any());
    }
}
