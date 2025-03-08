package io.github.persdsr.taskmanagementsystem.repository;

import io.github.persdsr.taskmanagementsystem.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<CommentEntity, Long> {
}
