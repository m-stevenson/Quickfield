package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.TaskRequest;
import ca.skyfield.quickfield.dto.TaskResponse;
import ca.skyfield.quickfield.model.Task;
import ca.skyfield.quickfield.model.enums.TaskState;
import ca.skyfield.quickfield.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {
        log.debug("Creating new task {}", taskRequest.id());

        Task task = Task.builder()
                .title(taskRequest.title())
                .description(taskRequest.description())
                .taskState(TaskState.TODO)
                .build();

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getTaskState(),
                List.of()
        );
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        log.debug("Retrieving all tasks");

        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(this::mapToTaskResponse)
                .toList();
    }

    private TaskResponse mapToTaskResponse(Task task){
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getTaskState(),
                List.of()
        );
    }

    @Override
    public Long updateTask(Long taskId, TaskRequest taskRequest) {
        log.debug("Updating task with id {}", taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        task.setTaskState(taskRequest.taskState());

        return taskRepository.save(task).getId();
    }

    @Override
    public void deleteTask(Long taskId) {
        log.debug("Deleting task with id {}", taskId);

        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("Task not found");
        }

        taskRepository.deleteById(taskId);
    }
}
