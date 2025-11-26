package com.jiralite.mapper;

import com.jiralite.comment.Comment;
import com.jiralite.comment.CommentResponse;

public class CommentMapper {
    
    public static CommentResponse toResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setTaskId(comment.getTask().getId());
        response.setAuthorName(comment.getAuthor().getUsername());
        response.setCreatedAt(comment.getCreatedAt());
        return response;
    }
}
