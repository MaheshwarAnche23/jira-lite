package com.jiralite.task;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {
    
    private final TaskService taskService;
    
    @PostMapping("/api/projects/{projectId}/tasks")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateTaskRequest request) {
        TaskResponse response = taskService.createTask(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PatchMapping("/api/tasks/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskStatusRequest request) {
        TaskResponse response = taskService.updateTaskStatus(taskId, request.getStatus());
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/api/tasks/{taskId}/assignee")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskResponse> assignTask(
            @PathVariable Long taskId,
            @Valid @RequestBody AssignTaskRequest request) {
        TaskResponse response = taskService.assignTask(taskId, request.getAssigneeId());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/api/tasks")
    public ResponseEntity<List<TaskResponse>> getTasks(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) TaskStatus status) {
        List<TaskResponse> tasks = taskService.getTasks(projectId, assigneeId, status);
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/api/tasks/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long taskId) {
        TaskResponse response = taskService.getTaskById(taskId);
        return ResponseEntity.ok(response);
    }
}
