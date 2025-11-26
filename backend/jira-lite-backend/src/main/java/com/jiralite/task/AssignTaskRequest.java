package com.jiralite.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignTaskRequest {
    
    @NotNull(message = "Assignee ID is required")
    @Positive(message = "Assignee ID must be positive")
    private Long assigneeId;
}
