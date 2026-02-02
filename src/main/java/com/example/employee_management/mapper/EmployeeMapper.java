package com.example.employee_management.mapper;

import com.example.employee_management.dto.EmployeeRequestDto;
import com.example.employee_management.dto.EmployeeResponseDto;
import com.example.employee_management.entity.Employee;

public class EmployeeMapper {

    // DTO -> Entity
    public static Employee toEntity(EmployeeRequestDto dto) {

        Employee emp = new Employee();
        emp.setName(dto.getName());
        emp.setEmail(dto.getEmail());
        emp.setDepartment(dto.getDepartment());
        emp.setSalary(dto.getSalary());

        return emp;
    }

    // Entity -> DTO
    public static EmployeeResponseDto toDto(Employee emp) {

        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setName(emp.getName());
        dto.setEmail(emp.getEmail());
        dto.setDepartment(emp.getDepartment());
        dto.setSalary(emp.getSalary());

        return dto;
    }

    public static void updateEntity(Employee emp,EmployeeRequestDto requestDto){

        emp.setName(requestDto.getName());
        emp.setEmail(requestDto.getEmail());
        emp.setDepartment(requestDto.getDepartment());
        emp.setSalary(requestDto.getSalary());
    }
}
