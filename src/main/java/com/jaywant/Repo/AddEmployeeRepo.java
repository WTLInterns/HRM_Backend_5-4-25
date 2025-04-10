// package com.jaywant.Repo;

// import java.util.List;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;
// import com.jaywant.Model.AddEmployee;

// @Repository
// public interface AddEmployeeRepo extends JpaRepository<AddEmployee, Integer> {

//     AddEmployee findByEmail(String email);

//     AddEmployee findById(int empId);

//     AddEmployee findByFirstName(String firstName);

//     // Full name search (case-insensitive)
//     @Query("SELECT e FROM AddEmployee e WHERE lower(e.firstName) = lower(:firstName) AND lower(e.lastName) = lower(:lastName)")
//     AddEmployee findByFirstNameAndLastName(@Param("firstName") String firstName,
//             @Param("lastName") String lastName);

//     // Filter employees by company name
//     List<AddEmployee> findByCompany(String company);
// }

package com.jaywant.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaywant.Model.AddEmployee;

@Repository
public interface AddEmployeeRepo extends JpaRepository<AddEmployee, Integer> {

    AddEmployee findByEmail(String email);

    AddEmployee findById(int empId);

    AddEmployee findByFirstName(String firstName);

    @Query("SELECT e FROM AddEmployee e WHERE lower(e.firstName) = lower(:firstName) AND lower(e.lastName) = lower(:lastName)")
    AddEmployee findByFirstNameAndLastName(@Param("firstName") String firstName,
            @Param("lastName") String lastName);

    @Query("SELECT e FROM AddEmployee e WHERE CONCAT(lower(e.firstName), ' ', lower(e.lastName)) = lower(:fullName)")
    AddEmployee findByFullName(@Param("fullName") String fullName);

    List<AddEmployee> findByCompany(String company);
}
