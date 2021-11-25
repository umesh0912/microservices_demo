package com.uj.employee.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uj.employee.model.Employee;
import com.uj.employee.repository.EmployeeRepository;

@RestController
@RequestMapping("/employee")
public class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepo;

	@GetMapping("/all")
	public ResponseEntity<?> getAllTutorials() {
		try {
			List<Employee> employees = new ArrayList<Employee>();

			employeeRepo.findAll().forEach(employees::add);

			if (employees.isEmpty()) {
				return new ResponseEntity<String>("Employees data not available !", HttpStatus.INTERNAL_SERVER_ERROR);
			}

			return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Some Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/save")
	public ResponseEntity<?> saveEmployee(@RequestBody Employee employee) {
		try {
			Employee emp = employeeRepo.save(new Employee(employee.getEmpId(), employee.getEmpName()));
			return new ResponseEntity<Employee>(emp, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<String>("Some Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable("id") Integer id, @RequestBody Employee employee) {
		Optional<Employee> employeeData = employeeRepo.findById(id);

		if (employeeData.isPresent()) {
			Employee emp = employeeData.get();
			emp.setEmpId(employee.getEmpId());
			emp.setEmpName(employee.getEmpName());
			return new ResponseEntity<Employee>(employeeRepo.save(emp), HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Some Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable("id") Integer id) {
		try {
			employeeRepo.deleteById(id);
			return new ResponseEntity<String>("deleted employee id " + id, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Some Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
