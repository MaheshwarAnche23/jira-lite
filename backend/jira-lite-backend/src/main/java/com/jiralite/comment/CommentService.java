package com.jiralite.comment;

import com.jiralite.task.Task;
import com.jiralite.task.TaskRepository;
import com.jiralite.user.User;
import com.jiralite.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public CommentResponse createComment(Long taskId, CreateCommentRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setTask(task);
        comment.setAuthor(author);
        
        Comment savedComment = commentRepository.save(comment);
        
        return mapToResponse(savedComment);
    }
    
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByTaskId(Long taskId) {
        List<Comment> comments = commentRepository.findByTaskId(taskId);
        return comments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setTaskId(comment.getTask().getId());
        response.setAuthorName(comment.getAuthor().getUsername());
        response.setCreatedAt(comment.getCreatedAt());
        return response;
    }
}
