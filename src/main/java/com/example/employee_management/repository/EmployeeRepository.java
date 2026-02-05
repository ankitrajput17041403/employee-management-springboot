package com.example.employee_management.repository;

import com.example.employee_management.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    //“JpaRepository provides built-in CRUD operations and reduces boilerplate DAO code.”

    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findByDepartmentContainingIgnoreCase(String department);

    List<Employee> findByNameContainingIgnoreCaseAndDepartmentContainingIgnoreCase(
            String name, String department);

    Page<Employee> findByNameContainingIgnoreCase(String name,Pageable pageable);

    Page<Employee> findByDepartmentContainingIgnoreCase(String department,Pageable pageable);

    Page<Employee> findByNameContainingIgnoreCaseAndDepartmentContainingIgnoreCase(
            String name, String department, Pageable pageable);



}
