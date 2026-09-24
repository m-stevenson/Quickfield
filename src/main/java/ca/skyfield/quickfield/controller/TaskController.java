package ca.skyfield.quickfield.controller;

import ca.skyfield.quickfield.dto.TaskRequest;
import ca.skyfield.quickfield.dto.TaskResponse;
import ca.skyfield.quickfield.model.Task;
import ca.skyfield.quickfield.service.TaskServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private TaskServiceImpl taskService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest taskRequest
    ) {
        TaskResponse taskResponse = taskService.createTask(taskRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);

    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(
            @PathVariable("taskId") Long taskId,
            @Valid @RequestBody TaskRequest taskRequest
    ) {
        Long updatedTaskId = taskService.updateTask(taskId, taskRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/tasks/" + updatedTaskId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(
            @PathVariable("taskId") Long taskId
    ) {
        taskService.deleteTask(taskId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
