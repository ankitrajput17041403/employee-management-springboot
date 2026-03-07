package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeRequestDto;
import com.example.employee_management.dto.EmployeeResponseDto;
import com.example.employee_management.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@Validated
@RequestMapping("/api/employees")
@Tag(name = "Employee APIs", description = "Operations related to employee management")
public class EmployeeController {

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(
            summary = "Create new employee",
            description = "Creates and saves a new employee into database"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto employeeRequestDto){

        logger.info("Create employee request received");

        EmployeeResponseDto employeeResponseDto = employeeService.createEmployee(employeeRequestDto);

        logger.info("Employee created successfully with name {}", employeeRequestDto.getName());
        return new ResponseEntity<>(employeeResponseDto, HttpStatus.CREATED);
    }


    @Operation(
            summary = "Get all employees",
            description = "Returns list of all employees. Returns empty list if no employees found"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employees fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees(){

        logger.info("Fetching all employees");

        return ResponseEntity.ok(employeeService.getAllEmployees());
    }


    @Operation(summary = "Get employee by ID",
            description = "Fetch employee details using employee id")

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee found"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id){

        logger.info("Fetching employee with id {}", id);

        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }


    @Operation(summary = "Delete employee by ID",
            description = "Delete employee By using employee id")

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee found And Deleted"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> deleteEmployeeById(@PathVariable Long id){

        logger.warn("Deleting employee with id {}", id);

        return ResponseEntity.ok(employeeService.deleteEmployeeById(id));
    }


    @Operation(summary = "Update employee by ID",
            description = "Update employee By using employee id")

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee Updated "),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "400", description = "Validation Failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployeeById(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDto employeeRequestDto){

        logger.info("Updating employee with id {}", id);

        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeRequestDto));
    }


    @Operation(summary = "Get employees with pagination",
            description = "Returns employees with page number, page size and sorting support"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employees fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/page")
    public ResponseEntity<Page<EmployeeResponseDto>> getEmployeesWithPagination(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "id") String sortBy){

        logger.info("Fetching employees with pagination: page={}, size={}, sortBy={}", page, size, sortBy);

        return ResponseEntity.ok(employeeService.getEmployees(page, size, sortBy));
    }

//
//    @GetMapping("/search/name")
//    public ResponseEntity<List<EmployeeResponseDto>> searchByName(
//            @RequestParam String name) {
//        return ResponseEntity.ok(employeeService.searchByName(name));
//    }
//
//    @GetMapping("/search/department")
//    public ResponseEntity<List<EmployeeResponseDto>> searchByDepartment(
//            @RequestParam  String department){
//        return ResponseEntity.ok(employeeService.searchByDepartment(department));
//
//
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<EmployeeResponseDto>> searchByDepartment(
//            @RequestParam(required = false)  String name, @RequestParam(required = false) String department){
//        return ResponseEntity.ok(employeeService.searchEmployees(name,department));
//
//
//    }


    @Operation(summary = "Search employees",
            description = "Search employees by name or department with pagination support"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employees fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeResponseDto>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size) {

        logger.info("Searching employees with name={} department={}", name, department);

        return ResponseEntity.ok(employeeService.searchEmployees(name, department, page, size));
    }


}

