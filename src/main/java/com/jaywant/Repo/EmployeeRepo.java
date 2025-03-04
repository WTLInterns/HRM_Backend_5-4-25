package com.jaywant.Repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.jaywant.Model.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer>{
 public Employee findByEmail(String email);
 
 
}
