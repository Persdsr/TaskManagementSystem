package io.github.persdsr.taskmanagementsystem.exception.handler;

import io.github.persdsr.taskmanagementsystem.exception.Error;
import io.github.persdsr.taskmanagementsystem.exception.ErrorResponse;
import io.github.persdsr.taskmanagementsystem.exception.response.*;
import io.github.persdsr.taskmanagementsystem.model.Code;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Task not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthorNotFound(AuthorNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Author not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("User not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthorNotFound(EmailNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Email not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.BAD_REQUEST).message("Invalid parameter value").build()).build(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.CONFLICT).message("Username already exists").build()).build(), HttpStatus.CONFLICT);

    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.CONFLICT).message("Email already exists").build()).build(), HttpStatus.CONFLICT);

    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return new ResponseEntity<>("Endpoint not found: " + ex.getRequestURL(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
