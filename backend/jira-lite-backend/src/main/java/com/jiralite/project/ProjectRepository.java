package com.jiralite.project;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    List<Project> findByCreatedById(Long userId);
    
    @EntityGraph(attributePaths = {"tasks", "createdBy"})
    Optional<Project> findById(Long id);
}
