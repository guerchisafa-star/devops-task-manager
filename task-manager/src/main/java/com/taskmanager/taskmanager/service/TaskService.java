package com.taskmanager.taskmanager.service;

import com.taskmanager.taskmanager.dto.TaskRequestDTO;
import com.taskmanager.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.taskmanager.model.Priority;
import com.taskmanager.taskmanager.model.Status;
import com.taskmanager.taskmanager.model.Task;
import com.taskmanager.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    // ── Créer une tâche ──────────────────────────────────────────────────────
    public TaskResponseDTO createTask(TaskRequestDTO dto) {
        Task task = taskMapper.toEntity(dto);
        Task saved = taskRepository.save(task);
        return taskMapper.toDTO(saved);
    }

    // ── Récupérer toutes les tâches ──────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ── Récupérer une tâche par ID ───────────────────────────────────────────
    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(Long id) {
        Task task = findOrThrow(id);
        return taskMapper.toDTO(task);
    }

    // ── Modifier une tâche ───────────────────────────────────────────────────
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO dto) {
        Task task = findOrThrow(id);

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setPriority(dto.getPriority() != null ? dto.getPriority() : Priority.MEDIUM);
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.TODO);
        // id et createdAt ne sont pas modifiés (règles 7 du cahier des charges)

        Task updated = taskRepository.save(task);
        return taskMapper.toDTO(updated);
    }

    // ── Supprimer une tâche ──────────────────────────────────────────────────
    public void deleteTask(Long id) {
        Task task = findOrThrow(id);
        taskRepository.delete(task);
    }

    // ── Utilitaire ───────────────────────────────────────────────────────────
    private Task findOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
