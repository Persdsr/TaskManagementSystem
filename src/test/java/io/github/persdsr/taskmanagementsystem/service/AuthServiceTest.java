package io.github.persdsr.taskmanagementsystem.service;

import io.github.persdsr.taskmanagementsystem.entity.UserEntity;
import io.github.persdsr.taskmanagementsystem.exception.response.EmailAlreadyExistsException;
import io.github.persdsr.taskmanagementsystem.exception.response.UsernameAlreadyExistsException;
import io.github.persdsr.taskmanagementsystem.model.request.SignUpRequestDTO;
import io.github.persdsr.taskmanagementsystem.repository.UserRepo;
import io.github.persdsr.taskmanagementsystem.security.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AuthService authService;

    private SignUpRequestDTO signUpRequest;

    @BeforeEach
    public void setUp() {
        signUpRequest = SignUpRequestDTO.builder()
                .username("testUsername")
                .email("email@mail.ru")
                .password("Password1234").build();
    }

    @Test
    public void testSignUp_newUser_returnsSuccessResponse() {

        when(encoder.encode(signUpRequest.getPassword())).thenReturn("EncodedPassword1234");
        when(userRepo.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(userRepo.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepo.save(any(UserEntity.class))).thenReturn(new UserEntity());

        ApiResponse response = authService.signUp(signUpRequest);

        assertTrue(response.isSuccess());
        assertEquals("User registered successfully!", response.getMessage());
        verify(userRepo, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testSignUp_UsernameAlreadyExists_ThrowsUsernameAlreadyExistsException() {

        when(userRepo.existsByUsername(anyString())).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> authService.signUp(signUpRequest));
        verify(userRepo, never()).save(any(UserEntity.class));
    }

    @Test
    void testSignUp_ConstraintViolation_ReturnsFailureResponse() {
        when(userRepo.existsByUsername(anyString())).thenReturn(false);
        when(userRepo.existsByEmail(anyString())).thenReturn(false);
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any(UserEntity.class))).thenThrow(new ConstraintViolationException("Constraint violation", null));

        ApiResponse response = authService.signUp(signUpRequest);

        assertFalse(response.isSuccess());
        assertEquals("Something went wrong!", response.getMessage());
    }

}
