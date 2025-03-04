package com.jaywant.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaywant.Model.AddEmployee;

import jakarta.persistence.Entity;

@Repository
public interface AddEmployeeRepo extends JpaRepository<AddEmployee, Integer>{

	AddEmployee findByEmail(String email);
	
	AddEmployee findById(int empId);
	
	AddEmployee findByFirstName(String firstName);


}
