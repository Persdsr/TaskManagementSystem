package io.github.persdsr.taskmanagementsystem.entity;

import io.github.persdsr.taskmanagementsystem.model.TaskPriority;
import io.github.persdsr.taskmanagementsystem.model.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "task")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.LOW;

    @ManyToOne()
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @ManyToOne()
    @JoinColumn(name = "performer_id")
    private UserEntity performer;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();

}
