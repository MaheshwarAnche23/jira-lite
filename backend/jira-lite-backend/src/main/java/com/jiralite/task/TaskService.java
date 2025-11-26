package com.jiralite.task;

import com.jiralite.mapper.TaskMapper;
import com.jiralite.project.Project;
import com.jiralite.project.ProjectRepository;
import com.jiralite.user.User;
import com.jiralite.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public TaskResponse createTask(Long projectId, CreateTaskRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM);
        task.setStatus(TaskStatus.TODO);
        task.setDueDate(request.getDueDate());
        task.setProject(project);
        
        if (request.getAssigneeId() != null && request.getAssigneeId() > 0) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElse(null);
            if (assignee != null) {
                task.setAssignee(assignee);
            }
        }
        
        Task savedTask = taskRepository.save(task);
        log.info("Task created: id={}, title={}, projectId={}, assigneeId={}", 
                savedTask.getId(), savedTask.getTitle(), projectId, 
                savedTask.getAssignee() != null ? savedTask.getAssignee().getId() : null);
        
        return TaskMapper.toResponse(savedTask);
    }
    
    @Transactional
    public TaskResponse updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        TaskStatus oldStatus = task.getStatus();
        
        // Check if user has permission to change status
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isAdminOrManager = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));
        boolean isAssignee = task.getAssignee() != null && task.getAssignee().getUsername().equals(username);
        
        if (!isAdminOrManager && !isAssignee) {
            throw new RuntimeException("Access denied: Only assignee or admin/manager can change task status");
        }
        
        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);
        log.info("Task status changed: taskId={}, oldStatus={}, newStatus={}, changedBy={}", 
                taskId, oldStatus, status, username);
        
        return TaskMapper.toResponse(updatedTask);
    }
    
    @Transactional
    public TaskResponse assignTask(Long taskId, Long assigneeId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String assignedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        task.setAssignee(assignee);
        Task updatedTask = taskRepository.save(task);
        log.info("Task assigned: taskId={}, assigneeId={}, assignedBy={}", 
                taskId, assigneeId, assignedBy);
        
        return TaskMapper.toResponse(updatedTask);
    }
    
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasks(Long projectId, Long assigneeId, TaskStatus status) {
        List<Task> tasks;
        if (projectId != null && status != null) {
            tasks = taskRepository.findByProjectIdAndStatus(projectId, status, Pageable.unpaged()).getContent();
        } else if (projectId != null) {
            tasks = taskRepository.findByProjectId(projectId);
        } else if (assigneeId != null) {
            tasks = taskRepository.findByAssigneeId(assigneeId);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status);
        } else {
            tasks = taskRepository.findAll();
        }
        
        return tasks.stream()
                .map(TaskMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        return TaskMapper.toResponse(task);
    }
}
