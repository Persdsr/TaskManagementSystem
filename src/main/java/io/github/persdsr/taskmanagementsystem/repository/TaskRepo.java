package io.github.persdsr.taskmanagementsystem.repository;

import io.github.persdsr.taskmanagementsystem.entity.TaskEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends JpaRepository<TaskEntity, Integer>, JpaSpecificationExecutor<TaskEntity> {
    Optional<List<TaskEntity>> findAllByAuthorUsername(String username, Pageable pageable);
    Optional<List<TaskEntity>> findAllByPerformerUsername(String username);
    TaskEntity findByAuthorUsername(String username);
}
