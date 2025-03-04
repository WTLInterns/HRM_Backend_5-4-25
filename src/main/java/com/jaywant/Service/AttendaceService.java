package com.jaywant.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jaywant.Model.AddEmployee;
import com.jaywant.Model.Attendance;
import com.jaywant.Repo.AddEmployeeRepo;
import com.jaywant.Repo.AttendanceRepo;

@Service
public class AttendaceService {

    @Autowired
    private AttendanceRepo attendanceRepo;
    
    @Autowired
    private AddEmployeeRepo employeeRepo;
    
    // Create a new attendance record for the employee using the employee's first name.
    // If an attendance record for the same employee and date already exists, update its status.
    public Attendance createOrUpdateAttendance(Attendance att) {
        String firstName = att.getEmployee().getFirstName();
        AddEmployee employee = this.employeeRepo.findByFirstName(firstName);
        if (employee == null) {
            throw new IllegalArgumentException("Employee with first name " + firstName + " not found.");
        }
        att.setEmployee(employee);

        // Check for an existing attendance record for this employee and date.
        List<Attendance> existingRecords =
                attendanceRepo.findByEmployeeEmpIdAndDate(employee.getEmpId(), att.getDate());

        if (!existingRecords.isEmpty()) {
            // Assume only one record per day per employee; update its status.
            Attendance existing = existingRecords.get(0);
            existing.setStatus(att.getStatus());
            existing.setDate(att.getDate());
            return attendanceRepo.save(existing);
        } else {
            // Otherwise, create a new record.
            return attendanceRepo.save(att);
        }
    }
    
    // Get all attendance records
    public List<Attendance> getAllAttendace() {
        return this.attendanceRepo.findAll();
    }
    
    // Get attendance records for a specific employee by id
    public List<Attendance> getAllAttendance(int empId) {
        return this.attendanceRepo.findByEmployeeEmpId(empId);
    }
    
    // Find attendance records by employee first name
    public List<Attendance> getAttendanceByEmployeeName(String firstName) {
        return attendanceRepo.findByEmployeeFirstName(firstName);
    }
    
    // Update the status for a given attendance record by its id
    public Attendance updateAttendanceStatus(Long id, String status) {
        Attendance existing = attendanceRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found with id: " + id));
        existing.setStatus(status);
        return attendanceRepo.save(existing);
    }
    
    // Update attendance status by employee id and date.
    // Throws an exception if no record exists or if multiple records are found.
    public Attendance updateAttendanceStatusByEmployeeAndDate(int empId, String date, String newStatus) {
        List<Attendance> records = attendanceRepo.findByEmployeeEmpIdAndDate(empId, date);
        
        if (records.isEmpty()) {
            throw new IllegalArgumentException("No attendance record found for empId " + empId + " on date " + date);
        }
        
        if (records.size() > 1) {
            throw new IllegalStateException("Multiple attendance records found for empId " + empId + " on date " + date);
        }
        
        Attendance attendance = records.get(0);
        attendance.setStatus(newStatus);
        return attendanceRepo.save(attendance);
    }
}
