package com.taskmanagement.taskmanagement.domain.repositories;

import com.taskmanagement.taskmanagement.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByAuthorIdOrderByCreatedAtAsc(Long authorId);

    List<Task> findAllByAuthorIdAndStatusOrderByCreatedAtAsc(Long authorId, Integer status);

}
