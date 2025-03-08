package io.github.persdsr.taskmanagementsystem.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank(message = "Text is required")
    @Size(max = 1000, message = "Text must be less than 1000 characters")
    private String text;

}
