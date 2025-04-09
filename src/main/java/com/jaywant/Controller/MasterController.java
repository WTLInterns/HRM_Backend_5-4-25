package com.jaywant.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jaywant.Model.Admin;
import com.jaywant.Service.AdminPasswordResetService;
import com.jaywant.Service.AdminService;

@RestController
@RequestMapping("/admin")
public class MasterController {

  @Autowired
  private AdminService adminService;

  @Autowired
  private AdminPasswordResetService passwordResetService;

  // Registration endpoint (supports form-data)
  @PostMapping(value = "/register", consumes = { "multipart/form-data" })
  public ResponseEntity<?> registerAdmin(@ModelAttribute Admin admin) {
    try {
      Admin savedAdmin = adminService.registerAdmin(admin);
      return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // Login endpoint
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
    Optional<Admin> adminOpt = adminService.login(email, password);
    if (adminOpt.isPresent()) {
      return ResponseEntity.ok(adminOpt.get());
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
  }

  // Update admin (supports form-data)
  @PutMapping(value = "/update", consumes = "multipart/form-data")
  public ResponseEntity<?> updateAdmin(
      @RequestParam("id") Long id,
      @RequestParam("name") String name,
      @RequestParam("email") String email,
      @RequestParam("password") String password,
      @RequestParam("mobileno") long mobileno,
      @RequestParam("profileImg") MultipartFile profileImg) {

    // Build Admin object manually
    Admin admin = new Admin();
    admin.setId(id);
    admin.setName(name);
    admin.setEmail(email);
    admin.setPassword(password);
    admin.setMobileno(mobileno);

    try {
      // ✅ Pass the file to the correct service method
      Admin updatedAdmin = adminService.updateAdmin(admin, profileImg);
      return ResponseEntity.ok(updatedAdmin);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  // Find admin by email
  @GetMapping("/find")
  public ResponseEntity<?> findByEmail(@RequestParam String email) {
    Optional<Admin> adminOpt = adminService.findByEmail(email);
    if (adminOpt.isPresent()) {
      return ResponseEntity.ok(adminOpt.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
    }
  }

  // Delete admin by ID
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
    try {
      adminService.deleteAdmin(id);
      return ResponseEntity.ok("Admin deleted successfully");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  // Request OTP for password reset
  @PostMapping("/forgot-password/request")
  public ResponseEntity<?> requestForgotPassword(@RequestParam String email) {
    try {
      passwordResetService.sendResetOTP(email);
      return ResponseEntity.ok("OTP sent to email: " + email);
    } catch (RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
  }

  // Verify OTP and reset password
  @PostMapping("/forgot-password/verify")
  public ResponseEntity<?> verifyOtpAndResetPassword(
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
}
