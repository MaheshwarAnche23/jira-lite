package com.jiralite.project;

import com.jiralite.mapper.ProjectMapper;
import com.jiralite.user.User;
import com.jiralite.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreatedBy(currentUser);
        
        Project savedProject = projectRepository.save(project);
        log.info("Project created: id={}, name={}, createdBy={}", 
                savedProject.getId(), savedProject.getName(), username);
        
        return ProjectMapper.toResponse(savedProject);
    }
    
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(ProjectMapper::toResponse);
    }
    
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        return ProjectMapper.toResponse(project);
    }
}
