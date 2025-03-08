package io.github.persdsr.taskmanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Text is required")
    @Size(max = 1000, message = "Text must be less than 1000 characters")
    private String text;

    @ManyToOne()
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne()
    @JoinColumn(name = "task_id")
    private TaskEntity task;

}
