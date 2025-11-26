package com.jiralite.mapper;

import com.jiralite.project.Project;
import com.jiralite.project.ProjectResponse;

public class ProjectMapper {
    
    public static ProjectResponse toResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setCreatedBy(project.getCreatedBy().getUsername());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        response.setTaskCount(project.getTasks() != null ? project.getTasks().size() : 0);
        return response;
    }
}
