package com.jiralite.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    
    private Long id;
    private String text;
    private Long taskId;
    private Long authorId;
    private String authorName;
    private LocalDateTime createdAt;
}
