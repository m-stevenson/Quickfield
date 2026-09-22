package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.TaskRequest;
import ca.skyfield.quickfield.dto.TaskResponse;

import java.util.List;

public interface TaskService {

        TaskResponse createTask(TaskRequest taskRequest);

        List<TaskResponse> getAllTasks();

        Long updateTask(Long taskId, TaskRequest taskRequest);

        void deleteTask(Long taskId);

    }



