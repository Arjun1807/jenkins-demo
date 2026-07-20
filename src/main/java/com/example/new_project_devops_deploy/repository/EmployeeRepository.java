package com.example.new_project_devops_deploy.repository;

import com.example.new_project_devops_deploy.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}