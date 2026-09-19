package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.EmployeeRequest;
import ca.skyfield.quickfield.dto.EmployeeResponse;

import java.util.List;

public interface TaskService {

        EmployeeResponse createTask(EmployeeRequest employeeRequest);

        List<EmployeeResponse> getAllTasks();

        Long updateTask(Long employeeId, EmployeeRequest employeeRequest);

        void deleteTask(Long employeeId);


    }



