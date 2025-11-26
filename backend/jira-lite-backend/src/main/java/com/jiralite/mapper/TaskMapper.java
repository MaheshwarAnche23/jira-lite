package com.jiralite.mapper;

import com.jiralite.task.Task;
import com.jiralite.task.TaskResponse;

public class TaskMapper {
    
    public static TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setDueDate(task.getDueDate());
        response.setProjectId(task.getProject().getId());
        response.setProjectName(task.getProject().getName());
        response.setAssigneeName(task.getAssignee() != null ? task.getAssignee().getUsername() : null);
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        response.setCommentCount(task.getComments() != null ? task.getComments().size() : 0);
        return response;
    }
}
