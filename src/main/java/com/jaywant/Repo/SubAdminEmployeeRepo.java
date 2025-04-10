package com.jaywant.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaywant.Model.AddEmployee;

@Repository
public interface SubAdminEmployeeRepo extends JpaRepository<AddEmployee, Integer> {

  @Query("SELECT e FROM AddEmployee e WHERE lower(e.firstName) = lower(:firstName) " +
      "AND lower(e.lastName) = lower(:lastName) AND lower(e.company) = lower(:company)")
  AddEmployee findByFirstNameAndLastNameAndCompany(@Param("firstName") String firstName,
      @Param("lastName") String lastName,
      @Param("company") String company);
}
