package com.uj.employee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@Column(name ="empId")
	Integer empId;
	
	@Column(name ="empName")
	String empName;
	
	public Employee(Integer empId2, String empName2) {
		this.empId = empId2;
		this.empName = empName2;
	}
}
