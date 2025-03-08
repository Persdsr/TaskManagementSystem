package io.github.persdsr.taskmanagementsystem.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpRequestDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 30, message = "Username must be less than 30 characters")
    private String username;

    @Email(message = "Incorrect email")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 8, message = "The password must be 8 or more than 8 characters")
    @NotBlank(message = "Password is required")
    private String password;
}
