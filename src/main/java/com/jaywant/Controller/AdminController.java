package com.jaywant.Controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jaywant.DTO.SalaryDTO;
import com.jaywant.Model.AddEmployee;
import com.jaywant.Model.Attendance;
import com.jaywant.Service.AttendaceService;
import com.jaywant.Service.EmployeeEmailService;
import com.jaywant.Service.EmployeePasswordResetService;
import com.jaywant.Service.EmployeeService;
import com.jaywant.Service.SalaryService;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private EmployeeService empService;

    @Autowired
    private AttendaceService attService;

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private EmployeeEmailService employeeEmailService;

    @Autowired
    private EmployeePasswordResetService employeePasswordResetService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // -------------------------------
    // Employee CRUD & Query Endpoints
    // -------------------------------

    // Add Employee (Admin)
    @PostMapping("/addEmp")
    public ResponseEntity<AddEmployee> addEmployee(@RequestBody AddEmployee addEmp) {
        AddEmployee newEmployee = empService.addEmp(addEmp);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
    }

    // Get all employees
    @GetMapping("/getAllEmp")
    public List<AddEmployee> getAllEmp() {
        return empService.getAllEmployee();
    }

    // Find employee by name
    @GetMapping("/find/{name}")
    public ResponseEntity<AddEmployee> findByName(@PathVariable String name) {
        try {
            AddEmployee employee = empService.findByEmployeeName(name);
            return ResponseEntity.ok(employee);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Update employee by ID
    @PutMapping("/update/{empId}")
    public AddEmployee updateEmp(@PathVariable int empId, @RequestBody AddEmployee addEmp) {
        return empService.updateEmployee(addEmp, empId);
    }

    // Delete employee by ID (also deletes related attendance records)
    @DeleteMapping("/deleteEmp/{empId}")
    public void deleteEmpById(@PathVariable int empId) {
        empService.deleteEmpId(empId);
        System.out.println("Deleted successfully: " + empId);
    }

    // -------------------------------
    // Attendance Endpoints
    // -------------------------------

    // Create or update attendance
    @PostMapping("/att")
    public ResponseEntity<Attendance> createOrUpdateAttendance(@RequestBody Attendance attendance) {
        try {
            Attendance updatedAttendance = attService.createOrUpdateAttendance(attendance);
            return new ResponseEntity<>(updatedAttendance, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Get all attendance records
    @GetMapping("/getAllAtt")
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        try {
            List<Attendance> attendanceList = attService.getAllAttendace();
            return ResponseEntity.ok(attendanceList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get attendance by employee ID
    @GetMapping("/getAllAttendace/{empId}")
    public List<Attendance> getAttendanceByEmployee(@PathVariable int empId) {
        return attService.getAllAttendance(empId);
    }

    // Search attendance by employee name
    @GetMapping("/getAttendanceByName/{name:.+}")
    public ResponseEntity<List<Attendance>> getAttendanceByName(@PathVariable("name") String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String[] parts = name.trim().split("\\s+");
        List<Attendance> attendanceList;
        if (parts.length >= 2) {
            String firstName = parts[0];
            String lastName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
            attendanceList = attService.getAttendanceByEmployeeName(firstName, lastName);
        } else {
            attendanceList = attService.getAttendanceByEmployeeName(parts[0]);
        }
        if (attendanceList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(attendanceList);
        }
        return ResponseEntity.ok(attendanceList);
    }

    // Update attendance status by attendance ID
    @PutMapping("/updateAttendanceStatus/{id}")
    public ResponseEntity<Attendance> updateAttendanceStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Attendance updatedAttendance = attService.updateAttendanceStatus(id, status);
            return ResponseEntity.ok(updatedAttendance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update attendance status by employee ID and date
    @PutMapping("/updateAttendanceStatusByEmpAndDate/{empId}/{date}/{newStatus}")
    public ResponseEntity<Attendance> updateAttendanceStatusByEmpAndDate(@PathVariable int empId,
            @PathVariable String date, @PathVariable String newStatus) {
        try {
            Attendance updatedAttendance = attService.updateAttendanceStatusByEmployeeAndDate(empId, date, newStatus);
            return ResponseEntity.ok(updatedAttendance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // -------------------------------
    // Salary Report Endpoint
    // -------------------------------

    @GetMapping("/generateReport")
    public SalaryDTO generateSalaryReport(@RequestParam String employeeName,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        System.out.println("generateReport called with: employeeName=" + employeeName +
                ", startDate=" + startDate + ", endDate=" + endDate);
        return salaryService.generateSalaryReport(employeeName, startDate, endDate);
    }

    // -------------------------------
    // Employee Forgot Password & Email Endpoints
    // -------------------------------

    // Request forgot password OTP
    @PostMapping("/employee/forgot-password/request")
    public ResponseEntity<String> requestEmployeeForgotPassword(@RequestParam String email) {
        try {
            employeePasswordResetService.sendResetOTP(email);
            return ResponseEntity.ok("OTP sent to email: " + email);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // Verify OTP and reset password
    @PostMapping("/employee/forgot-password/verify")
    public ResponseEntity<String> verifyEmployeeOtpAndResetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        if (!employeePasswordResetService.verifyOTP(email, otp)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }
        try {
            employeePasswordResetService.resetPassword(email, newPassword);
            return ResponseEntity.ok("Password updated successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // Send employee registration email
    @PostMapping("/send-email/employee")
    public ResponseEntity<String> sendEmployeeRegisterEmail(@RequestParam String email) {
        AddEmployee employee = empService.getEmployeeByEmail(email);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
        employeeEmailService.sendEmployeeCredentials(employee);
        return ResponseEntity.ok("Employee registration email sent to: " + email);
    }
}
