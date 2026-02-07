package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequestDto;
import com.example.employee_management.dto.EmployeeResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto);
    List<EmployeeResponseDto> getAllEmployees();
    EmployeeResponseDto getEmployeeById(Long id);
    EmployeeResponseDto deleteEmployeeById(Long id);
    EmployeeResponseDto updateEmployee(Long id,EmployeeRequestDto employeeRequestDto);
    public Page<EmployeeResponseDto> getEmployees(int page, int size, String sortBy);

    // For List ---------------
    List<EmployeeResponseDto> searchByName(String name);

    List<EmployeeResponseDto> searchByDepartment(String department);

    List<EmployeeResponseDto> searchEmployees(String name, String department);

   //Search + pageignation

    Page<EmployeeResponseDto> searchEmployees(String name, String department,int page,int size);


}
