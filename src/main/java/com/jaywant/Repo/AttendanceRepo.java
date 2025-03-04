package com.jaywant.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.jaywant.Model.Attendance;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance, Long> {

    // Find attendances for an employee within a date range.
    List<Attendance> findByEmployeeEmpIdAndDateBetween(int empId, String startDate, String endDate);

    // Find all attendance records by employee id.
    List<Attendance> findByEmployeeEmpId(int empId);

    // Find attendance records by employee first name
    List<Attendance> findByEmployeeFirstName(String firstName);
    
    // Find attendance records by employee id and date.
    // This method returns a List so that duplicate records can be handled.
    List<Attendance> findByEmployeeEmpIdAndDate(int empId, String date);
}
