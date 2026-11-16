package com.taskmanager.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.taskmanager.dto.TaskRequestDTO;
import com.taskmanager.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.taskmanager.model.Priority;
import com.taskmanager.taskmanager.model.Status;
import com.taskmanager.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private TaskResponseDTO sampleResponse;
    private TaskRequestDTO sampleRequest;

    @BeforeEach
    void setUp() {
        sampleResponse = TaskResponseDTO.builder()
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
    }

    // ── POST /api/tasks ──────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/tasks — 201 avec le corps de la tâche créée")
    void createTask_returns201() throws Exception {
        when(taskService.createTask(any(TaskRequestDTO.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Rédiger le rapport"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    @DisplayName("POST /api/tasks — 400 si le titre est absent")
    void createTask_missingTitle_returns400() throws Exception {
        TaskRequestDTO invalid = TaskRequestDTO.builder().description("Sans titre").build();

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
    }

    // ── GET /api/tasks ───────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/tasks — 200 avec liste des tâches")
    void getAllTasks_returns200() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Rédiger le rapport"));
    }

    @Test
    @DisplayName("GET /api/tasks — 200 avec liste vide")
    void getAllTasks_empty_returns200() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ── GET /api/tasks/{id} ──────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/tasks/{id} — 200 pour un ID valide")
    void getTaskById_found_returns200() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /api/tasks/{id} — 404 pour un ID inexistant")
    void getTaskById_notFound_returns404() throws Exception {
        when(taskService.getTaskById(99L)).thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("99")));
    }

    // ── PUT /api/tasks/{id} ──────────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/tasks/{id} — 200 avec la tâche mise à jour")
    void updateTask_returns200() throws Exception {
        TaskResponseDTO updated = TaskResponseDTO.builder()
                .id(1L)
                .title("Rapport final")
                .status(Status.IN_PROGRESS)
                .priority(Priority.MEDIUM)
                .build();

        when(taskService.updateTask(eq(1L), any(TaskRequestDTO.class))).thenReturn(updated);

        TaskRequestDTO updateRequest = TaskRequestDTO.builder()
                .title("Rapport final")
                .priority(Priority.MEDIUM)
                .status(Status.IN_PROGRESS)
                .build();

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Rapport final"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("PUT /api/tasks/{id} — 404 si ID inexistant")
    void updateTask_notFound_returns404() throws Exception {
        when(taskService.updateTask(eq(99L), any(TaskRequestDTO.class)))
                .thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(put("/api/tasks/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isNotFound());
    }

    // ── DELETE /api/tasks/{id} ───────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/tasks/{id} — 204 après suppression")
    void deleteTask_returns204() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} — 404 si ID inexistant")
    void deleteTask_notFound_returns404() throws Exception {
        doThrow(new TaskNotFoundException(99L)).when(taskService).deleteTask(99L);

        mockMvc.perform(delete("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }
}
