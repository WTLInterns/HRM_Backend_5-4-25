package com.jaywant.Controller;

import com.jaywant.DTO.EmployeeWithAttendanceDTO;
import com.jaywant.Model.AddSubAdmin;
import com.jaywant.Model.Attendance;
import com.jaywant.Model.Employee;
import com.jaywant.Repo.AttendanceRepo;
import com.jaywant.Repo.EmployeeRepo;
import com.jaywant.Service.AddSubAdminService;
import com.jaywant.Service.SubAdminPasswordResetService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jaywant.Repo.AddSubAdminRepository;

@RestController
@RequestMapping("/api/subadmin")
@CrossOrigin
public class SubAdminController {

  @Autowired
  private AddSubAdminService service;

  @Autowired
  private SubAdminPasswordResetService passwordResetService;

  // Endpoint for creating a sub-admin with file uploads.
  @PostMapping("/create")
  public ResponseEntity<?> createSubAdmin(
      @RequestParam String name,
      @RequestParam String lastname,
      @RequestParam String email,
      @RequestParam String phoneno,
      @RequestParam String registercompanyname,
      @RequestParam("stampImg") MultipartFile stampImgFile,
      @RequestParam("signature") MultipartFile signatureFile,
      @RequestParam("companylogo") MultipartFile logoFile) {

    AddSubAdmin subAdmin = new AddSubAdmin();
    subAdmin.setName(name);
    subAdmin.setLastname(lastname);
    subAdmin.setEmail(email);
    subAdmin.setPhoneno(phoneno);
    subAdmin.setRegistercompanyname(registercompanyname);

    try {
      AddSubAdmin savedSubAdmin = service.createSubAdmin(subAdmin, stampImgFile, signatureFile, logoFile);
      return ResponseEntity.ok(savedSubAdmin);
    } catch (RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
    AddSubAdmin user = service.login(email, password);
    if (user != null) {
      return ResponseEntity.ok(user);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
  }

  @PutMapping("/update-password/{id}")
  public ResponseEntity<String> updatePassword(@PathVariable int id, @RequestParam String newPassword) {
    service.updatePassword(id, newPassword);
    return ResponseEntity.ok("Password updated successfully");
  }

  // Endpoint to send email manually.
  @PostMapping("/send-email/{subadminemail}")
  public ResponseEntity<String> sendEmail(@PathVariable("subadminemail") String subadminemail) {
    boolean sent = service.sendSubAdminEmail(subadminemail);
    if (sent) {
      return ResponseEntity.ok("Email sent successfully to " + subadminemail);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SubAdmin with email " + subadminemail + " not found.");
    }
  }

  // GET endpoint to retrieve all sub-admin information.
  @GetMapping("/all")
  public ResponseEntity<List<AddSubAdmin>> getAllSubAdmins() {
    List<AddSubAdmin> subAdmins = service.getAllSubAdmins();
    return ResponseEntity.ok(subAdmins);
  }

  // GET endpoint to retrieve a specific sub-admin by email.
  @GetMapping("/subadminbygamil/{email}")
  public ResponseEntity<?> getSubAdminByEmail(@PathVariable("email") String email) {
    Optional<AddSubAdmin> subAdminOpt = service.getSubAdminByEmail(email);
    if (subAdminOpt.isPresent()) {
      return ResponseEntity.ok(subAdminOpt.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("SubAdmin with email " + email + " not found.");
    }
  }

  // DELETE endpoint to delete a sub-admin by ID.
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteSubAdmin(@PathVariable("id") int id) {
    Optional<AddSubAdmin> subAdminOpt = service.getSubAdminById(id);
    if (subAdminOpt.isPresent()) {
      service.deleteSubAdmin(id);
      return ResponseEntity.ok("SubAdmin with ID " + id + " deleted successfully.");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("SubAdmin with ID " + id + " not found.");
    }
  }

  // ----- New Endpoints for OTP Based Password Reset -----

  // Endpoint to request an OTP for password reset.
  @PostMapping("/forgot-password/request")
  public ResponseEntity<String> requestForgotPassword(@RequestParam String email) {
    try {
      passwordResetService.sendResetOTP(email);
      return ResponseEntity.ok("OTP sent to email: " + email);
    } catch (RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
  }

  // Endpoint to verify OTP and update the password.
  @PostMapping("/forgot-password/verify")
  public ResponseEntity<String> verifyOtpAndResetPassword(
      @RequestParam String email,
      @RequestParam String otp,
      @RequestParam String newPassword) {
    if (!passwordResetService.verifyOTP(email, otp)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
    }
    try {
      passwordResetService.resetPassword(email, newPassword);
      return ResponseEntity.ok("Password updated successfully.");
    } catch (RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
  }

  @Autowired
  private EmployeeRepo employeeRepo;

  @Autowired
  private AttendanceRepo attendanceRepo;

  @Autowired
  private AddSubAdminRepository addSubAdminRepo;

  @GetMapping("/by-company/{registercompanyname:.+}")
  public ResponseEntity<?> getEmployeesWithAttendanceByCompany(
      @PathVariable("registercompanyname") String companyName) {

    // First, try to find employees with the given company name
    List<Employee> employees = employeeRepo.findByCompany(companyName);

    if (!employees.isEmpty()) {
      List<EmployeeWithAttendanceDTO> result = new ArrayList<>();
      for (Employee emp : employees) {
        List<Attendance> attendanceList = attendanceRepo.findByEmployeeEmpId(emp.getEmpId());
        result.add(new EmployeeWithAttendanceDTO(emp, attendanceList));
      }
      return ResponseEntity.ok(result);
    }

    List<AddSubAdmin> subAdmins = addSubAdminRepo.findByRegistercompanyname(companyName);
    if (!subAdmins.isEmpty()) {
      // Company exists but no employees
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body("⚠️ No employees found for company: " + companyName + ". Please add employees first.");
    } else {
      // Company doesn't exist
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body("❌ No company found with name: " + companyName + ". Please register the company first.");
    }

  }

}
