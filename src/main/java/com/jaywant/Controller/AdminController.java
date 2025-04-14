// package com.jaywant.Controller;

// import java.util.Arrays;
// import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import com.jaywant.DTO.SalaryDTO;
// import com.jaywant.Model.AddEmployee;
// import com.jaywant.Model.Attendance;
// import com.jaywant.Service.AttendaceService;
// import com.jaywant.Service.EmployeeEmailService;
// import com.jaywant.Service.EmployeePasswordResetService;
// import com.jaywant.Service.EmployeeService;
// import com.jaywant.Service.SalaryService;

// @RestController
// @RequestMapping("/public")
// @CrossOrigin(origins = "http://localhost:3000")
// public class AdminController {

//     @Autowired
//     private EmployeeService empService;

//     @Autowired
//     private AttendaceService attService;

//     @Autowired
//     private SalaryService salaryService;

//     @Autowired
//     private EmployeeEmailService employeeEmailService;

//     @Autowired
//     private EmployeePasswordResetService employeePasswordResetService;

//     private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//     // -------------------------------
//     // Employee CRUD & Query Endpoints
//     // -------------------------------

//     // Add Employee (Admin)
//     @PostMapping("/addEmp")
//     public ResponseEntity<AddEmployee> addEmployee(@RequestBody AddEmployee addEmp) {
//         AddEmployee newEmployee = empService.addEmp(addEmp);
//         return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
//     }

//     // Get all employees
//     @GetMapping("/getAllEmp")
//     public List<AddEmployee> getAllEmp() {
//         return empService.getAllEmployee();
//     }

//     // Find employee by name
//     @GetMapping("/find/{name}")
//     public ResponseEntity<AddEmployee> findByName(@PathVariable String name) {
//         try {
//             AddEmployee employee = empService.findByEmployeeName(name);
//             return ResponseEntity.ok(employee);
//         } catch (IllegalArgumentException ex) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//         }
//     }

//     // Update employee by ID
//     @PutMapping("/update/{empId}")
//     public AddEmployee updateEmp(@PathVariable int empId, @RequestBody AddEmployee addEmp) {
//         return empService.updateEmployee(addEmp, empId);
//     }

//     // Delete employee by ID (also deletes related attendance records)
//     @DeleteMapping("/deleteEmp/{empId}")
//     public void deleteEmpById(@PathVariable int empId) {
//         empService.deleteEmpId(empId);
//         System.out.println("Deleted successfully: " + empId);
//     }

//     // -------------------------------
//     // Attendance Endpoints
//     // -------------------------------

//     // Create or update attendance
//     @PostMapping("/att")
//     public ResponseEntity<Attendance> createOrUpdateAttendance(@RequestBody Attendance attendance) {
//         try {
//             Attendance updatedAttendance = attService.createOrUpdateAttendance(attendance);
//             return new ResponseEntity<>(updatedAttendance, HttpStatus.CREATED);
//         } catch (IllegalArgumentException e) {
//             return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//         }
//     }

//     // Get all attendance records
//     @GetMapping("/getAllAtt")
//     public ResponseEntity<List<Attendance>> getAllAttendance() {
//         try {
//             List<Attendance> attendanceList = attService.getAllAttendace();
//             return ResponseEntity.ok(attendanceList);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//         }
//     }

//     // Get attendance by employee ID
//     @GetMapping("/getAllAttendace/{empId}")
//     public List<Attendance> getAttendanceByEmployee(@PathVariable int empId) {
//         return attService.getAllAttendance(empId);
//     }

//     // Search attendance by employee name
//     @GetMapping("/getAttendanceByName/{name:.+}")
//     public ResponseEntity<List<Attendance>> getAttendanceByName(@PathVariable("name") String name) {
//         if (name == null || name.trim().isEmpty()) {
//             return ResponseEntity.badRequest().build();
//         }
//         String[] parts = name.trim().split("\\s+");
//         List<Attendance> attendanceList;
//         if (parts.length >= 2) {
//             String firstName = parts[0];
//             String lastName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
//             attendanceList = attService.getAttendanceByEmployeeName(firstName, lastName);
//         } else {
//             attendanceList = attService.getAttendanceByEmployeeName(parts[0]);
//         }
//         if (attendanceList.isEmpty()) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(attendanceList);
//         }
//         return ResponseEntity.ok(attendanceList);
//     }

//     // Update attendance status by attendance ID
//     @PutMapping("/updateAttendanceStatus/{id}")
//     public ResponseEntity<Attendance> updateAttendanceStatus(@PathVariable Long id, @RequestParam String status) {
//         try {
//             Attendance updatedAttendance = attService.updateAttendanceStatus(id, status);
//             return ResponseEntity.ok(updatedAttendance);
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.notFound().build();
//         }
//     }

//     // Update attendance status by employee ID and date
//     @PutMapping("/updateAttendanceStatusByEmpAndDate/{empId}/{date}/{newStatus}")
//     public ResponseEntity<Attendance> updateAttendanceStatusByEmpAndDate(@PathVariable int empId,
//             @PathVariable String date, @PathVariable String newStatus) {
//         try {
//             Attendance updatedAttendance = attService.updateAttendanceStatusByEmployeeAndDate(empId, date, newStatus);
//             return ResponseEntity.ok(updatedAttendance);
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//         }
//     }

//     // -------------------------------
//     // Salary Report Endpoint
//     // -------------------------------

//     @GetMapping("/generateReport")
//     public SalaryDTO generateSalaryReport(@RequestParam String employeeName,
//             @RequestParam String startDate,
//             @RequestParam String endDate) {
//         System.out.println("generateReport called with: employeeName=" + employeeName +
//                 ", startDate=" + startDate + ", endDate=" + endDate);
//         return salaryService.generateSalaryReport(employeeName, startDate, endDate);
//     }

//     // -------------------------------
//     // Employee Forgot Password & Email Endpoints
//     // -------------------------------

//     // Request forgot password OTP
//     @PostMapping("/employee/forgot-password/request")
//     public ResponseEntity<String> requestEmployeeForgotPassword(@RequestParam String email) {
//         try {
//             employeePasswordResetService.sendResetOTP(email);
//             return ResponseEntity.ok("OTP sent to email: " + email);
//         } catch (RuntimeException ex) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//         }
//     }

//     // Verify OTP and reset password
//     @PostMapping("/employee/forgot-password/verify")
//     public ResponseEntity<String> verifyEmployeeOtpAndResetPassword(
//             @RequestParam String email,
//             @RequestParam String otp,
//             @RequestParam String newPassword) {
//         if (!employeePasswordResetService.verifyOTP(email, otp)) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
//         }
//         try {
//             employeePasswordResetService.resetPassword(email, newPassword);
//             return ResponseEntity.ok("Password updated successfully.");
//         } catch (RuntimeException ex) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//         }
//     }

//     // Send employee registration email
//     @PostMapping("/send-email/employee")
//     public ResponseEntity<String> sendEmployeeRegisterEmail(@RequestParam String email) {
//         AddEmployee employee = empService.getEmployeeByEmail(email);
//         if (employee == null) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
//         }
//         employeeEmailService.sendEmployeeCredentials(employee);
//         return ResponseEntity.ok("Employee registration email sent to: " + email);
//     }
// }

package com.jaywant.Controller;

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
import com.jaywant.DTO.SubAdminWithEmployeesDTO;
import com.jaywant.Model.AddEmployee;
import com.jaywant.Model.Attendance;
import com.jaywant.Service.AddSubAdminService;
import com.jaywant.Service.AttendaceService;
import com.jaywant.Service.EmployeeEmailService;
import com.jaywant.Service.EmployeePasswordResetService;
import com.jaywant.Service.EmployeeService;
import com.jaywant.Service.SalaryService;

@RestController
@RequestMapping("/api/subadmin")
@CrossOrigin(origins = "*")
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
    private AddSubAdminService subAdminService;

    @Autowired
    private EmployeePasswordResetService employeePasswordResetService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // =====================================================
    // Employee Management (CRUD)
    // =====================================================

    // POST: Add an employee
    // URL: POST /api/subadmin/employee/{companyName}/add
    @PostMapping("/employee/{companyName}/add")
    public ResponseEntity<AddEmployee> addEmployee(
            @PathVariable String companyName,
            @RequestBody AddEmployee addEmp) {

        addEmp.setCompany(companyName);
        AddEmployee newEmployee = empService.addEmp(addEmp);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
    }

    // GET: Get all employees by company
    // URL: GET /api/subadmin/employee/{companyName}/all
    // @GetMapping("/employee/{companyName}/all")
    // public ResponseEntity<List<AddEmployee>>
    // getAllEmployeesByCompany(@PathVariable String companyName) {
    // List<AddEmployee> employees = empService.getEmployeesByCompany(companyName);
    // return ResponseEntity.ok(employees);
    // }

    @GetMapping("/employees/{companyName}/all")
    public ResponseEntity<List<AddEmployee>> getEmployeesByCompany(@PathVariable String companyName) {
        List<AddEmployee> employees = empService.getEmployeesByCompany(companyName);
        if (employees == null || employees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(employees);
        }
        return ResponseEntity.ok(employees);
    }

    // GET: Find an employee by full name
    // URL: GET /api/subadmin/employee/{companyName}/find?fullName={fullName}
    @GetMapping("/employee/{companyName}/find")
    public ResponseEntity<AddEmployee> findEmployeeByName(
            @PathVariable String companyName,
            @RequestParam String fullName) {
        AddEmployee employee = empService.findByEmployeeName(fullName);
        if (employee == null || !employee.getCompany().equalsIgnoreCase(companyName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(employee);
    }

    // PUT: Update an existing employee by full name
    // URL: PUT /api/subadmin/employee/{companyName}/update?fullName={fullName}
    @PutMapping("/employee/{companyName}/update")
    public ResponseEntity<AddEmployee> updateEmployee(
            @PathVariable String companyName,
            @RequestParam String fullName,
            @RequestBody AddEmployee addEmp) {

        AddEmployee existing = empService.findByEmployeeName(fullName);
        if (existing == null || !existing.getCompany().equalsIgnoreCase(companyName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        addEmp.setCompany(companyName);
        addEmp.setEmpId(existing.getEmpId());
        AddEmployee updatedEmployee = empService.updateEmployee(addEmp, addEmp.getEmpId());
        return ResponseEntity.ok(updatedEmployee);
    }

    // DELETE: Delete an employee by full name
    // URL: DELETE /api/subadmin/employee/{companyName}/delete?fullName={fullName}
    @DeleteMapping("/employee/{companyName}/delete")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable String companyName,
            @RequestParam String fullName) {
        AddEmployee employee = empService.findByEmployeeName(fullName);
        if (employee == null || !employee.getCompany().equalsIgnoreCase(companyName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found or unauthorized");
        }
        empService.deleteEmpId(employee.getEmpId());
        return ResponseEntity.ok("Deleted successfully: " + fullName);
    }

    // =====================================================
    // Attendance APIs
    // =====================================================

    // POST: Mark attendance for an employee
    // URL: POST
    // /api/subadmin/employee/{companyName}/{employeeFullName}/attendance/add
    @PostMapping("/employee/{companyName}/{employeeFullName}/attendance/add")
    public ResponseEntity<String> markAttendance(
            @PathVariable String companyName,
            @PathVariable String employeeFullName,
            @RequestBody Attendance attendance) {

        AddEmployee employee = empService.findByEmployeeName(employeeFullName);
        if (employee == null || !employee.getCompany().equalsIgnoreCase(companyName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee " + employeeFullName + " not found in company " + companyName);
        }

        attendance.setEmployee(employee);
        attService.createOrUpdateAttendance(attendance);

        return ResponseEntity.ok("✅ Attendance added for " + employeeFullName + " on " + attendance.getDate());
    }

    // GET: Get attendance records for an employee
    // URL: GET /api/subadmin/employee/{companyName}/{employeeFullName}/attendance
    @GetMapping("/employee/{companyName}/{employeeFullName}/attendance")
    public ResponseEntity<List<Attendance>> getAttendanceByEmployee(
            @PathVariable String companyName,
            @PathVariable String employeeFullName) {

        AddEmployee employee = empService.findByEmployeeName(employeeFullName);
        if (employee == null || !employee.getCompany().equalsIgnoreCase(companyName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Attendance> attendanceList = attService.getAllAttendance(employee.getEmpId());
        return ResponseEntity.ok(attendanceList);
    }

    // PUT: Update an attendance record by id
    // URL: PUT
    // /api/subadmin/employee/{companyName}/{employeeFullName}/attendance/update/{id}?status=newStatus
    @PutMapping("/employee/{companyName}/{employeeFullName}/attendance/update/{id}")
    public ResponseEntity<Attendance> updateAttendanceStatus(
            @PathVariable String companyName,
            @PathVariable String employeeFullName,
            @PathVariable Long id,
            @RequestParam String status) {

        try {
            Attendance updatedAttendance = attService.updateAttendanceStatus(id, status);
            return ResponseEntity.ok(updatedAttendance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT: Update attendance status by employee and date
    // URL: PUT
    // /api/subadmin/employee/{companyName}/{employeeFullName}/attendance/update/date/{date}/{newStatus}
    @PutMapping("/employee/{companyName}/{employeeFullName}/attendance/update/date/{date}/{newStatus}")
    public ResponseEntity<Attendance> updateAttendanceByDate(
            @PathVariable String companyName,
            @PathVariable String employeeFullName,
            @PathVariable String date,
            @PathVariable String newStatus) {

        AddEmployee employee = empService.findByEmployeeName(employeeFullName);
        if (employee == null || !employee.getCompany().equalsIgnoreCase(companyName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            Attendance updated = attService.updateAttendanceStatusByEmployeeAndDate(employee.getEmpId(), date,
                    newStatus);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // =====================================================
    // Salary Report API
    // =====================================================

    // GET: Generate salary report for an employee based on attendance dates
    // URL: GET
    // /api/subadmin/employee/{companyName}/{employeeFullName}/attendance/report?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd
    @GetMapping("/employee/{companyName}/{employeeFullName}/attendance/report")
    public ResponseEntity<SalaryDTO> generateSalaryReport(
            @PathVariable String companyName,
            @PathVariable String employeeFullName,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        AddEmployee employee = empService.findByEmployeeName(employeeFullName);
        if (employee == null || !employee.getCompany().equalsIgnoreCase(companyName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        SalaryDTO report = salaryService.generateSalaryReport(employeeFullName, startDate, endDate);
        return ResponseEntity.ok(report);
    }

    // =====================================================
    // Forgot Password / Email APIs
    // =====================================================

    // POST: Request OTP for forgot password
    // URL: POST /api/subadmin/employee/forgot-password/request?email={email}
    @PostMapping("/employee/forgot-password/request")
    public ResponseEntity<String> requestOtp(@RequestParam String email) {
        try {
            employeePasswordResetService.sendResetOTP(email);
            return ResponseEntity.ok("OTP sent to email: " + email);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // POST: Verify OTP and reset password
    // URL: POST
    // /api/subadmin/employee/forgot-password/verify?email={email}&otp={otp}&newPassword={newPassword}
    @PostMapping("/employee/forgot-password/verify")
    public ResponseEntity<String> verifyOtpAndReset(
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

    // POST: Send registration email to employee
    // URL: POST /api/subadmin/send-email/employee?email={email}
    @PostMapping("/send-email/employee")
    public ResponseEntity<String> sendRegistrationEmail(@RequestParam String email) {
        AddEmployee employee = empService.getEmployeeByEmail(email);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }

        employeeEmailService.sendEmployeeCredentials(employee);
        return ResponseEntity.ok("Employee registration email sent to: " + email);
    }
}
