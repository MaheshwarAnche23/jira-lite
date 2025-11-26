package com.jiralite.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    @EntityGraph(attributePaths = {"project", "assignee", "comments"})
    List<Task> findByProjectId(Long projectId);
    
    @EntityGraph(attributePaths = {"project", "assignee", "comments"})
    List<Task> findByAssigneeId(Long assigneeId);
    
    @EntityGraph(attributePaths = {"project", "assignee", "comments"})
    List<Task> findByStatus(TaskStatus status);
    
    @EntityGraph(attributePaths = {"project", "assignee", "comments"})
    Page<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status, Pageable pageable);
    
    @EntityGraph(attributePaths = {"project", "assignee", "comments"})
    Optional<Task> findById(Long id);
}
