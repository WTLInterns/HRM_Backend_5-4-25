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
    private AddEmployeeRepo addEmployeeRepo;
    
    // Create or update attendance record.
    public Attendance createOrUpdateAttendance(Attendance att) {
        // Check if the passed employee is transient (i.e. no empId)
        if (att.getEmployee() == null || att.getEmployee().getEmpId() == 0) {
            // Expect the full name to be provided in the firstName field.
            String fullName = att.getEmployee() != null ? att.getEmployee().getFirstName() : "";
            String[] parts = fullName.trim().split("\\s+");
            AddEmployee employee;
            if (parts.length >= 2) {
                String firstName = parts[0];
                String lastName = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length));
                employee = addEmployeeRepo.findByFirstNameAndLastName(firstName, lastName);
            } else {
                employee = addEmployeeRepo.findByFirstName(fullName);
            }
            if (employee == null) {
                throw new IllegalArgumentException("No employee found with name: " + fullName);
            }
            att.setEmployee(employee);
        }
        
        // Now that att.getEmployee() is a persistent entity, check for an existing record.
        List<Attendance> existingRecords = attendanceRepo.findByEmployeeEmpIdAndDate(
                att.getEmployee().getEmpId(), att.getDate());
        
        if (!existingRecords.isEmpty()) {
            // Update existing record (assumes one record per day per employee)
            Attendance existing = existingRecords.get(0);
            existing.setStatus(att.getStatus());
            existing.setDate(att.getDate());
            return attendanceRepo.save(existing);
        } else {
            return attendanceRepo.save(att);
        }
    }
    
    public List<Attendance> getAllAttendace() {
        return attendanceRepo.findAll();
    }
    
    public List<Attendance> getAllAttendance(int empId) {
        return attendanceRepo.findByEmployeeEmpId(empId);
    }
    
    // When both first and last names are provided.
    public List<Attendance> getAttendanceByEmployeeName(String firstName, String lastName) {
        return attendanceRepo.findByEmployeeFirstNameAndLastName(firstName, lastName);
    }
    
    // Fallback when only a single name is provided.
    public List<Attendance> getAttendanceByEmployeeName(String firstName) {
        return attendanceRepo.findByEmployeeFirstName(firstName);
    }
    
    public Attendance updateAttendanceStatus(Long id, String status) {
        Attendance existing = attendanceRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Attendance not found with id: " + id));
        existing.setStatus(status);
        return attendanceRepo.save(existing);
    }
    
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
