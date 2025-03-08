package io.github.persdsr.taskmanagementsystem.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse implements Response {
    private Error error;
}