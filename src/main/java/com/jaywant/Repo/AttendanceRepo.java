// package com.jaywant.Repo;

// import java.util.List;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;
// import com.jaywant.Model.Attendance;

// @Repository
// public interface AttendanceRepo extends JpaRepository<Attendance, Long> {

//     List<Attendance> findByEmployeeEmpIdAndDateBetween(int empId, String startDate, String endDate);

//     List<Attendance> findByEmployeeEmpId(int empId);

//     List<Attendance> findByEmployeeEmpIdAndDate(int empId, String date);

//     // Case‑insensitive search by first name only.
//     @Query("SELECT a FROM Attendance a WHERE lower(a.employee.firstName) = lower(:firstName)")
//     List<Attendance> findByEmployeeFirstName(@Param("firstName") String firstName);

//     // Case‑insensitive search by first and last name.
//     @Query("SELECT a FROM Attendance a WHERE lower(a.employee.firstName) = lower(:firstName) AND lower(a.employee.lastName) = lower(:lastName)")
//     List<Attendance> findByEmployeeFirstNameAndLastName(@Param("firstName") String firstName,
//                                                         @Param("lastName") String lastName);
// }

package com.jaywant.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaywant.Model.Attendance;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance, Long> {

    // Find by employee id
    List<Attendance> findByEmployeeEmpId(int empId);

    // Find attendance records for an employee between dates (assuming date is
    // stored as String in yyyy-MM-dd)
    List<Attendance> findByEmployeeEmpIdAndDateBetween(int empId, String startDate, String endDate);
}
