package io.github.persdsr.taskmanagementsystem.controller;

import io.github.persdsr.taskmanagementsystem.model.request.SignInRequestDTO;
import io.github.persdsr.taskmanagementsystem.model.request.SignUpRequestDTO;
import io.github.persdsr.taskmanagementsystem.security.ApiResponse;
import io.github.persdsr.taskmanagementsystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "AUTH")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signin")
    @Operation(
            summary = "Авторизация пользователя",
            description = """
        Аутентифицирует пользователя по email и паролю. В случае успешной аутентификации возвращает JWT токен, который можно использовать для доступа к защищенным эндпоинтам.

        ### Параметры:
        - **email**: Электронная почта пользователя (обязательно).
        - **password**: Пароль пользователя (обязательно).

        ### Возвращаемое значение:
        - **JWT токен**: Токен для доступа к защищенным эндпоинтам.

        ### Пример запроса:
        ```json
        {
            "email": "user@example.com",
            "password": "password1234"
        }
        ```

        ### Пример ответа:
        ```json
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        ```

        ### Возможные ошибки:
        - **400 Bad Request**: Некорректные данные (например, отсутствует email или пароль).
        - **401 Unauthorized**: Неверные учетные данные (email или пароль).
        """
    )
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody SignInRequestDTO loginRequest) {
        String jwt = authService.signIn(loginRequest);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/signup")
    @Operation(
            summary = "Регистрация нового пользователя",
            description = """
        Регистрирует нового пользователя в системе. При успешной регистрации возвращает ответ с сообщением о успешном завершении операции.

        ### Параметры:
        - **name**: Имя пользователя (обязательно, должен быть уникальным).
        - **email**: Электронная почта пользователя (обязательно, должен быть уникальным).
        - **password**: Пароль пользователя (обязательно, минимум 8 символов).

        ### Возвращаемое значение:
        - **message**: Сообщение о успешной регистрации.
        - **status**: Статус операции (например, "success").

        ### Пример запроса:
        ```json
        {
            "username": "Username",
            "email": "user@example.com",
            "password": "password1234"
        }
        ```

        ### Пример ответа:
        ```json
        {
            "message": "User registered successfully",
            "status": "success"
        }
        ```

        ### Возможные ошибки:
        - **400 Bad Request**: Некорректные данные (например, отсутствует email или пароль).
        - **409 Conflict**: Пользователь с таким email уже существует.
        """
    )
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequestDTO signUpRequest) {
        return ResponseEntity.ok().body(authService.signUp(signUpRequest));
    }
}
