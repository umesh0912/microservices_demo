package com.uj.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uj.employee.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
