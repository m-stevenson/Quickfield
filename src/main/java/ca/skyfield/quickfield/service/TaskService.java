package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.task.TaskRequest;
import ca.skyfield.quickfield.dto.task.TaskResponse;
import ca.skyfield.quickfield.model.enums.TaskState;

import java.util.List;

public interface TaskService {

        TaskResponse createTask(TaskRequest taskRequest);

        List<TaskResponse> getAllTasks();

        Long updateTask(Long taskId, TaskRequest taskRequest);

        Long updateTaskState(Long taskId, TaskState taskState);

        void deleteTask(Long taskId);

    }



