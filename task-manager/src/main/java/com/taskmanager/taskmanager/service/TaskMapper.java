package com.taskmanager.taskmanager.service;

import com.taskmanager.taskmanager.dto.TaskRequestDTO;
import com.taskmanager.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.taskmanager.model.Priority;
import com.taskmanager.taskmanager.model.Status;
import com.taskmanager.taskmanager.model.Task;
import org.springframework.stereotype.Component;

/**
 * Conversion entre l'entité Task et les DTOs.
 * Centralisé ici pour ne pas polluer la couche service ou contrôleur.
 */
@Component
public class TaskMapper {

    public TaskResponseDTO toDTO(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .build();
    }

    public Task toEntity(TaskRequestDTO dto) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .priority(dto.getPriority() != null ? dto.getPriority() : Priority.MEDIUM)
                .status(dto.getStatus() != null ? dto.getStatus() : Status.TODO)
                .build();
    }
}
