package io.github.persdsr.taskmanagementsystem.exception.response;

import io.github.persdsr.taskmanagementsystem.model.Code;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class TaskNotFoundException extends RuntimeException {
    private final Code code;
    private final String message;
    private final HttpStatus httpStatus;
}
