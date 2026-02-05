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
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private EmployeeRepository employeeRepository;

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

          Employee employee =  EmployeeMapper.toEntity(requestDto);
          Employee saveEmployee = employeeRepository.save(employee);

        return EmployeeMapper.toDto(saveEmployee);
    }

    @Override
    public List<EmployeeResponseDto> getAllEmployees() {
       List<Employee> employees = employeeRepository.findAll();
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
        return employees.stream().map(EmployeeMapper::toDto).toList();
    }

    @Override
    public EmployeeResponseDto getEmployeeById(Long id) {
        Employee singleEmplData = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("This Id Employee Is NOT! Avilable" + id));

//        EmployeeResponseDto dto = new EmployeeResponseDto();
//        dto.setName(singleEmplData.getName());
//        dto.setEmail(singleEmplData.getEmail());
//        dto.setDepartment(singleEmplData.getDepartment());
//        dto.setSalary(singleEmplData.getSalary());
//        System.out.println("This Is The Name-----"+dto.getName());


        return EmployeeMapper.toDto(singleEmplData);
    }

    @Override
    public EmployeeResponseDto deleteEmployeeById(Long id) {
        Employee availableEmp =  employeeRepository.findById(id).orElseThrow(()->new EmployeeNotFoundException("This Id Employee Not Available"+id));
        EmployeeResponseDto employeeRequestDto = EmployeeMapper.toDto(availableEmp);

        employeeRepository.delete(availableEmp);

        return employeeRequestDto;
    }

    @Override
    public EmployeeResponseDto updateEmployee(Long id,EmployeeRequestDto employeeRequestDto) {
      Employee availablEmployee =   employeeRepository.findById(id).orElseThrow(()->new EmployeeNotFoundException("Employee Not Available From This Id"+id));

//
//      Old Approach.............................................
//      availablEmployee.setName(employeeRequestDto.getName());
//      availablEmployee.setEmail(employeeRequestDto.getEmail());
//      availablEmployee.setDepartment(employeeRequestDto.getDepartment());
//      availablEmployee.setSalary(employeeRequestDto.getSalary());

      EmployeeMapper.updateEntity(availablEmployee,employeeRequestDto);


      //Employee e= EmployeeMapper.toEntity(employeeRequestDto);
      Employee saveEmployeeOrUpdate = employeeRepository.save(availablEmployee);
      return EmployeeMapper.toDto(saveEmployeeOrUpdate);
    }

    @Override
    public Page<EmployeeResponseDto> getEmployees(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        return employeePage.map(EmployeeMapper::toDto);
    }


    @Override
    public List<EmployeeResponseDto> searchByName(String name) {

        List<Employee> employees =
                employeeRepository.findByNameContainingIgnoreCase(name);

        return employees.stream()
                .map(EmployeeMapper::toDto)
                .toList();
    }


    @Override
    public List<EmployeeResponseDto> searchByDepartment(String department) {

        List<Employee> employees =
                employeeRepository.findByDepartmentContainingIgnoreCase(department);

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

        } else if (name != null) {

            employees = employeeRepository
                    .findByNameContainingIgnoreCase(name);

        } else if (department != null) {

            employees = employeeRepository
                    .findByDepartmentContainingIgnoreCase(department);

        } else {

            employees = employeeRepository.findAll();
        }

        return employees.stream()
                .map(EmployeeMapper::toDto)
                .toList();
    }

    @Override
    public Page<EmployeeResponseDto> searchEmployees(String name, String department, int page, int size) {
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
