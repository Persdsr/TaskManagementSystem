package io.github.persdsr.taskmanagementsystem.controller;

import io.github.persdsr.taskmanagementsystem.model.TaskPriority;
import io.github.persdsr.taskmanagementsystem.model.TaskStatus;
import io.github.persdsr.taskmanagementsystem.model.request.CommentRequest;
import io.github.persdsr.taskmanagementsystem.model.request.TaskPriorityRequest;
import io.github.persdsr.taskmanagementsystem.model.request.TaskStatusRequest;
import io.github.persdsr.taskmanagementsystem.model.dto.task.TaskDTO;
import io.github.persdsr.taskmanagementsystem.model.dto.task.TaskRequestDTO;
import io.github.persdsr.taskmanagementsystem.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task")
@AllArgsConstructor
@Tag(name = "TASK")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить задачу по идентификатору",
            description = """
        Возвращает задачу по её идентификатору. Если задача не найдена, возвращается ошибка 404.

        ### Параметры:
        - **id**: Идентификатор задачи (обязательно).

        ### Возвращаемое значение:
        - Объект задачи в формате `TaskDTO`, содержащий:
          - **title**: Название задачи.
          - **description**: Описание задачи.
          - **author**: Имя автора задачи.
          - **performer**: Имя исполнителя задачи (если назначен).
          - **status**: Фильтр по статусу задачи (опционально). Значения: `PENDING (В ожидании)`, `IN_PROGRESS (В прогрессе)`, `COMPLETED (Завершен)`.
          - **priority**: Фильтр по приоритету задачи (опционально). Значения: `LOW (Низкий)`, `MEDIUM (Средний)`, `HIGH (Высокий)`.
          - **comments**: Список комментариев к задаче.

        ### Пример ответа:
        ```json
        {
            "title": "Fix bug in authentication",
            "description": "Fix the bug in the login endpoint",
            "author": "John",
            "performer": "Alice",
            "status": "IN_PROGRESS",
            "priority": "HIGH",
            "comments": [
                {
                    "text": "Comment text",
                    "author": "John"
                }
            ]
        }
        ```

        ### Возможные ошибки:
        - **401 Unauthorized**: Пользователь не авторизован.
        - **403 Forbidden**: У пользователя нет прав на просмотр задачи.
        - **404 Not Found**: Задача с указанным идентификатором не найдена.
        """
    )
    private ResponseEntity<TaskDTO> getTaskById(@PathVariable int id) {
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @GetMapping("")
    @Operation(
            summary = "Получить список фильтрованных задач",
            description = """
        Возвращает список задач с возможностью фильтрации по автору, исполнителю, статусу и приоритету. Поддерживает пагинацию.

        ### Параметры:
        - **author**: Фильтр по автору задачи (опционально).
        - **performer**: Фильтр по исполнителю задачи (опционально).
        - **status**: Фильтр по статусу задачи (опционально). Значения: `PENDING (В ожидании)`, `IN_PROGRESS (В прогрессе)`, `COMPLETED (Завершен)`.
        - **priority**: Фильтр по приоритету задачи (опционально). Значения: `LOW (Низкий)`, `MEDIUM (Средний)`, `HIGH (Высокий)`.
        - **page**: Номер страницы (по умолчанию 0).
        - **size**: Количество задач на странице (по умолчанию 5).

        ### Возвращаемое значение:
        - Список задач в формате `TaskDTO`.

        ### Пример запроса:
        ```
        GET /api/tasks?author=John&status=IN_PROGRESS&page=0&size=5
        ```

        ### Пример ответа:
        ```json
        [
            {
                "id": 1,
                "title": "Fix bug in authentication",
                "description": "Fix the bug in the login endpoint",
                "status": "IN_PROGRESS",
                "priority": "HIGH",
                "author": "John",
                "performer": "Alex"
            },
            {
                "id": 2,
                "title": "Update documentation",
                "description": "Update API documentation",
                "status": "NEW",
                "priority": "MEDIUM",
                "author": "John",
                "performer": "Bob"
            }
        ]
        ```

        ### Возможные ошибки:
        - **400 Bad Request**: Некорректные параметры запроса (например, неверный статус или приоритет).
        - **401 Unauthorized**: Пользователь не авторизован.
        - **403 Forbidden**: У пользователя нет прав на просмотр задач.
        """
    )
    private ResponseEntity<List<TaskDTO>> getTasks(
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "performer", required = false) String performer,
            @RequestParam(name = "status", required = false) TaskStatus status,
            @RequestParam(name = "priority", required = false) TaskPriority priority,
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize) {
        return new ResponseEntity<>(taskService.getTasks(author, performer, status, priority, pageNumber, pageSize), HttpStatus.OK);
    }


    @PostMapping()
    @Operation(
            summary = "Создать новую задачу",
            description = """
        Создает новую задачу на основе переданных данных. Возвращает сообщение об успешном создании задачи.

        ### Параметры:
        - **title**: Название задачи (обязательно).
        - **description**: Описание задачи (опционально).
        - **priority**: Приоритет задачи (опционально, по умолчанию LOW).  Значения: `LOW (Низкий)`, `MEDIUM (Средний)`, `HIGH (Высокий)`

        ### Пример запроса:
        ```json
        {
            "title": "Fix bug in authentication",
            "description": "Fix the bug in the login endpoint",
            "priority": "HIGH"
        }
        ```

        ### Возвращаемое значение:
        - Сообщение об успешном создании задачи.

        ### Пример ответа:
        ```
        "Task created"
        ```

        ### Возможные ошибки:
        - **400 Bad Request**: Некорректные данные.
        - **401 Unauthorized**: Пользователь не авторизован.
        - **403 Forbidden**: У пользователя нет прав на создание задачи.
        """
    )
    private ResponseEntity<String> createTask(@Valid @RequestBody TaskRequestDTO task) {
        taskService.addTask(task);
        return new ResponseEntity<>("Task created successfully", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Обновить задачу",
            description = """
        Обновляет задачу по её идентификатору. Принимает частичные данные для обновления задачи.

        ### Параметры:
        - **id**: Идентификатор задачи (обязательно).
        - **task**: Частичные данные для обновления задачи (обязательно). Поддерживаемые поля:
          - **title**: Название задачи.
          - **description**: Описание задачи.
          - **status**: Фильтр по статусу задачи (опционально). Значения: `PENDING (В ожидании)`, `IN_PROGRESS (В прогрессе)`, `COMPLETED (Завершен)`.
          - **priority**: Фильтр по приоритету задачи (опционально). Значения: `LOW (Низкий)`, `MEDIUM (Средний)`, `HIGH (Высокий)`.
          - **author**: Автор задачи.
          - **performer**: Исполнитель задачи.

        ### Пример запроса:
        ```json
        {
            "title": "Fix bug in authentication",
            "status": "IN_PROGRESS"
        }
        ```

        ### Возвращаемое значение:
        - Сообщение об успешном обновлении задачи.

        ### Пример ответа:
        ```
        "Task updated"
        ```

        ### Возможные ошибки:
        - **400 Bad Request**: Некорректные данные (например, неверный статус или приоритет).
        - **401 Unauthorized**: Пользователь не авторизован.
        - **403 Forbidden**: У пользователя нет прав на обновление задачи.
        - **404 Not Found**: Задача с указанным идентификатором не найдена.
        """
    )
    private ResponseEntity<String> updateTask(@Valid @RequestBody Map<String, Object> task,
                                              @PathVariable("id") int id) {
        taskService.updateTask(task, id);
        return new ResponseEntity<>("Task updated successfully", HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить задачу",
            description = """
        Удаляет задачу по её идентификатору. Возвращает сообщение об успешном удалении задачи.

        ### Параметры:
        - **id**: Идентификатор задачи (обязательно).

        ### Возвращаемое значение:
        - Сообщение об успешном удалении задачи.

        ### Пример ответа:
        ```
        "Task deleted successfully"
        ```

        ### Возможные ошибки:
        - **401 Unauthorized**: Пользователь не авторизован.
        - **403 Forbidden**: У пользователя нет прав на удаление задачи.
        - **404 Not Found**: Задача с указанным идентификатором не найдена.
        """
    )
    private ResponseEntity<String> deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }

    @PostMapping("/comment/{id}")
    @Operation(
            summary = "Добавить комментарий к задаче",
            description = """
        Добавляет комментарий к задаче по её идентификатору. Возвращает сообщение об успешном добавлении комментария.

        ### Параметры:
        - **taskId**: Идентификатор задачи (обязательно).
        - **comment**: Комментарий к задаче (обязательно). Поля:
          - **text**: Текст комментария (обязательно).

        ### Возвращаемое значение:
        - Сообщение об успешном добавлении комментария.

        ### Пример запроса:
        ```json
        {
            "text": "Пожалуйста, уточните требования к задаче."
        }
        ```

        ### Пример ответа:
        ```
        "Comment sent successfully"
        ```

        ### Возможные ошибки:
        - **400 Bad Request**: Некорректные данные (например, отсутствует текст комментария).
        - **401 Unauthorized**: Пользователь не авторизован.
        - **403 Forbidden**: У пользователя нет прав на добавление комментария.
        - **404 Not Found**: Задача с указанным идентификатором не найдена.
        """
    )
    private ResponseEntity<String> assignTask(@PathVariable("id") int taskId, @Valid @RequestBody CommentRequest comment) {
        taskService.addCommentToTask(taskId, comment);
        return new ResponseEntity<>("Comment sent successfully", HttpStatus.OK);
    }

}
