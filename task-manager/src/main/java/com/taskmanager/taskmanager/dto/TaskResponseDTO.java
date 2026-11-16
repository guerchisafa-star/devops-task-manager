package com.taskmanager.taskmanager.dto;

import com.taskmanager.taskmanager.model.Priority;
import com.taskmanager.taskmanager.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Représentation d'une tâche renvoyée au client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private Status status;
    private LocalDateTime createdAt;
}
