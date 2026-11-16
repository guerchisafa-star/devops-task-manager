package com.taskmanager.taskmanager.dto;

import com.taskmanager.taskmanager.model.Priority;
import com.taskmanager.taskmanager.model.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Payload reçu par le contrôleur pour créer ou modifier une tâche.
 * L'id ne fait pas partie de ce DTO : il ne peut pas être modifié par le client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequestDTO {

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    private String description;

    private LocalDate dueDate;

    private Priority priority;

    private Status status;
}
