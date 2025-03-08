package io.github.persdsr.taskmanagementsystem.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.persdsr.taskmanagementsystem.model.Code;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {

    private Code code;
    private String message;
}