package com.jiralite.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByProjectId(Long projectId);
    
    List<Task> findByAssigneeId(Long assigneeId);
    
    List<Task> findByStatus(TaskStatus status);
    
    Page<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status, Pageable pageable);
}
