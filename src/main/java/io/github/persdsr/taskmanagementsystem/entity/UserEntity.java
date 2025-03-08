package io.github.persdsr.taskmanagementsystem.entity;

import io.github.persdsr.taskmanagementsystem.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 30, message = "Username must be less than 30 characters")
    private String username;

    @Size(min = 8, message = "The password must be 8 or more than 8 characters")
    @NotBlank(message = "Password is required")
    private String password;

    @Email(message = "Incorrect email")
    @NotBlank(message = "Email is required")
    private String email;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<TaskEntity> tasks;

    @OneToMany(mappedBy = "performer")
    private List<TaskEntity> solvedTasks;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

}
