package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequestDto;
import com.example.employee_management.dto.EmployeeResponseDto;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.EmployeeNotFoundException;
import com.example.employee_management.mapper.EmployeeMapper;
import com.example.employee_management.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class EmployeeServiceImpl implements EmployeeService{

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository=employeeRepository;
    }

    @Override
    public EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto) {

//        //Dto->Entity.........
//        Employee employee = new Employee();
//        employee.setName(requestDto.getName());
//        employee.setEmail(requestDto.getEmail());
//        employee.setDepartment(requestDto.getDepartment());
//        employee.setSalary(requestDto.getSalary());
//
//        //Now Saved To DB............
//        Employee savedEmployee= employeeRepository.save(employee);
//
//
//        //Entity->Dto
//        EmployeeResponseDto responseDto = new EmployeeResponseDto();
//        responseDto.setName(savedEmployee.getName());
//        responseDto.setEmail(savedEmployee.getEmail());
//        responseDto.setDepartment(savedEmployee.getDepartment());
//        responseDto.setSalary(savedEmployee.getSalary());

        logger.info("Creating employee with email {}", requestDto.getEmail());

        Employee employee = EmployeeMapper.toEntity(requestDto);
        Employee saveEmployee = employeeRepository.save(employee);

        logger.info("Employee created successfully with id {}", saveEmployee.getId());

        return EmployeeMapper.toDto(saveEmployee);
    }

    @Override
    public List<EmployeeResponseDto> getAllEmployees() {

//        Old way-----------------
//        return employees.stream()
//                .map(employee -> {
//                    EmployeeResponseDto dto = new EmployeeResponseDto();
//                    dto.setName(employee.getName());
//                    dto.setEmail(employee.getEmail());
//                    dto.setDepartment(employee.getDepartment());
//                    dto.setSalary(employee.getSalary());
//                    return dto;
//                })
//                .collect(Collectors.toList());
        logger.info("Fetching all employees from database");

        List<Employee> employees = employeeRepository.findAll();

        logger.info("Total employees fetched {}", employees.size());

        return employees.stream().map(EmployeeMapper::toDto).toList();
    }

    @Override
    public EmployeeResponseDto getEmployeeById(Long id) {

//        EmployeeResponseDto dto = new EmployeeResponseDto();
//        dto.setName(singleEmplData.getName());
//        dto.setEmail(singleEmplData.getEmail());
//        dto.setDepartment(singleEmplData.getDepartment());
//        dto.setSalary(singleEmplData.getSalary());
//        System.out.println("This Is The Name-----"+dto.getName());

            logger.info("Fetching employee with id {}", id);

            Employee singleEmplData = employeeRepository.findById(id)
                    .orElseThrow(() -> new EmployeeNotFoundException("This Id Employee Is NOT! Available " + id));

            return EmployeeMapper.toDto(singleEmplData);
        }

    @Override
    public EmployeeResponseDto deleteEmployeeById(Long id) {

        logger.warn("Deleting employee with id {}", id);

        Employee availableEmp = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("This Id Employee Not Available " + id));

        EmployeeResponseDto employeeRequestDto = EmployeeMapper.toDto(availableEmp);

        employeeRepository.delete(availableEmp);

        logger.warn("Employee deleted with id {}", id);

        return employeeRequestDto;
    }

    @Override
    public EmployeeResponseDto updateEmployee(Long id,EmployeeRequestDto employeeRequestDto) {

//
//      Old Approach.............................................
//      availablEmployee.setName(employeeRequestDto.getName());
//      availablEmployee.setEmail(employeeRequestDto.getEmail());
//      availablEmployee.setDepartment(employeeRequestDto.getDepartment());
//      availablEmployee.setSalary(employeeRequestDto.getSalary());

        logger.info("Updating employee with id {}", id);

        Employee availablEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee Not Available From This Id " + id));

        EmployeeMapper.updateEntity(availablEmployee, employeeRequestDto);

        Employee saveEmployeeOrUpdate = employeeRepository.save(availablEmployee);

        logger.info("Employee updated successfully with id {}", id);

        return EmployeeMapper.toDto(saveEmployeeOrUpdate);
    }

    @Override
    public Page<EmployeeResponseDto> getEmployees(int page, int size, String sortBy) {

        logger.info("Fetching employees with pagination page={}, size={}, sortBy={}", page, size, sortBy);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        return employeePage.map(EmployeeMapper::toDto);
    }


    @Override
    public List<EmployeeResponseDto> searchByName(String name) {

        List<Employee> employees =
                employeeRepository.findByNameContainingIgnoreCase(name);
        logger.info("Searching employees by name {}", name);
        return employees.stream()
                .map(EmployeeMapper::toDto)
                .toList();
    }


    @Override
    public List<EmployeeResponseDto> searchByDepartment(String department) {

        List<Employee> employees =
                employeeRepository.findByDepartmentContainingIgnoreCase(department);
        logger.info("Searching employees by department {}", department);

        return employees.stream()
                .map(EmployeeMapper::toDto)
                .toList();
    }


    @Override
    public List<EmployeeResponseDto> searchEmployees(String name, String department) {

        List<Employee> employees;

        if (name != null && department != null) {

            employees = employeeRepository
                    .findByNameContainingIgnoreCaseAndDepartmentContainingIgnoreCase(name, department);
            logger.info("Searching employees with name={} and department={}", name, department);
        } else if (name != null) {

            employees = employeeRepository
                    .findByNameContainingIgnoreCase(name);
            logger.info("Searching employees with name={} ", name);

        } else if (department != null) {

            employees = employeeRepository
                    .findByDepartmentContainingIgnoreCase(department);
            logger.info("Searching employees with  department={}",  department);

        } else {
            logger.info("Searching employees without filters");

            employees = employeeRepository.findAll();
        }

        return employees.stream()
                .map(EmployeeMapper::toDto)
                .toList();
    }

    @Override
    public Page<EmployeeResponseDto> searchEmployees(String name, String department, int page, int size) {
        logger.info("Searching employees with pagination name={}, department={}, page={}, size={}",
                name, department, page, size);
        Pageable pageable = PageRequest.of(page, size);

        Page<Employee> employe;

        if(name!=null && department!=null){
            employe=employeeRepository.findByNameContainingIgnoreCaseAndDepartmentContainingIgnoreCase(name,department,pageable);
        } else if (name!=null) {
            employe=employeeRepository.findByNameContainingIgnoreCase(name,pageable);

        } else if (department!=null) {
            employe=employeeRepository.findByDepartmentContainingIgnoreCase(department,pageable);

        }
        else {
            employe=employeeRepository.findAll(pageable);
        }

        return employe.map(EmployeeMapper::toDto);
    }


}
