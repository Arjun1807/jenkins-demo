package com.example.new_project_devops_deploy.serviceimpl;

import com.example.new_project_devops_deploy.exception.EmployeeNotFoundException;
import com.example.new_project_devops_deploy.model.Employee;
import com.example.new_project_devops_deploy.repository.EmployeeRepository;
import com.example.new_project_devops_deploy.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Override
    public Employee saveEmployee(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @Override
    public Employee getEmployeeById(Integer id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee with ID " + id + " not found"));
    }

    @Override
    public Employee updateEmployee(Integer id, Employee employee) {

        Employee emp = repository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee with ID " + id + " not found"));

        emp.setName(employee.getName());
        emp.setDept(employee.getDept());
        emp.setSalary(employee.getSalary());

        return repository.save(emp);
    }

    @Override
    public void deleteEmployee(Integer id) {

        Employee emp = repository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee with ID " + id + " not found"));

        repository.delete(emp);
    }
}