package com.jaywant.Repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jaywant.Model.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer>{
 public Employee findByEmail(String email);
 
 
//Case-insensitive search by first and last name
 @Query("SELECT e FROM Employee e WHERE lower(e.firstName) = lower(:firstName) AND lower(e.lastName) = lower(:lastName)")
 List<Employee> findByFirstNameAndLastName(@Param("firstName") String firstName, 
                                           @Param("lastName") String lastName);

 
}
