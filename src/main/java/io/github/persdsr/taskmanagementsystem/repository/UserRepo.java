package io.github.persdsr.taskmanagementsystem.repository;

import io.github.persdsr.taskmanagementsystem.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
