package io.github.persdsr.taskmanagementsystem.service;

import io.github.persdsr.taskmanagementsystem.entity.UserEntity;
import io.github.persdsr.taskmanagementsystem.exception.response.EmailAlreadyExistsException;
import io.github.persdsr.taskmanagementsystem.exception.response.UsernameAlreadyExistsException;
import io.github.persdsr.taskmanagementsystem.model.request.SignInRequestDTO;
import io.github.persdsr.taskmanagementsystem.model.request.SignUpRequestDTO;
import io.github.persdsr.taskmanagementsystem.repository.UserRepo;
import io.github.persdsr.taskmanagementsystem.security.ApiResponse;
import io.github.persdsr.taskmanagementsystem.security.jwt.JwtUtils;
import io.github.persdsr.taskmanagementsystem.security.services.UserDetailsImpl;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;


    public String signIn(SignInRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return jwt;
    }

    public ApiResponse signUp(SignUpRequestDTO signUpRequest) {
        if (userRepo.existsByUsername(signUpRequest.getUsername())) {
            throw UsernameAlreadyExistsException.builder().build();
        }

        if (userRepo.existsByEmail(signUpRequest.getEmail())) {
            throw EmailAlreadyExistsException.builder().build();
        }

        UserEntity user = new UserEntity();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());

        try {
            userRepo.save(user);
            return new ApiResponse(true, "User registered successfully!");
        } catch (ConstraintViolationException e) {

            return new ApiResponse(false, "Something went wrong!");
        }
    }
}
