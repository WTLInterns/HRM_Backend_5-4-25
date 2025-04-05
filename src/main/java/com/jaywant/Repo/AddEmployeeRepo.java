package com.jaywant.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaywant.Model.AddEmployee;

import jakarta.persistence.Entity;

@Repository
public interface AddEmployeeRepo extends JpaRepository<AddEmployee, Integer>{

	AddEmployee findByEmail(String email);
	
	AddEmployee findById(int empId);
	
	AddEmployee findByFirstName(String firstName);


    // New method for full name search (case-insensitive)
    @Query("SELECT e FROM AddEmployee e WHERE lower(e.firstName) = lower(:firstName) AND lower(e.lastName) = lower(:lastName)")
    AddEmployee findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

	
}
