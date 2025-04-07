package com.jaywant.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.jaywant.Model.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

  Employee findByEmail(String email);

  // Use Optional if you prefer the standard method:
  // Optional<Employee> findById(Integer empId);
  Employee findById(int empId);

  // If needed for searching by first name (not commonly used in authentication)
  Employee findByFirstName(String firstName);

  // Full name search (case-insensitive)
  @Query("SELECT e FROM Employee e WHERE lower(e.firstName) = lower(:firstName) AND lower(e.lastName) = lower(:lastName)")
  Employee findByFirstNameAndLastName(@Param("firstName") String firstName,
      @Param("lastName") String lastName);

  // Filter employees by company name (if applicable)
  List<Employee> findByCompany(String company);
}
