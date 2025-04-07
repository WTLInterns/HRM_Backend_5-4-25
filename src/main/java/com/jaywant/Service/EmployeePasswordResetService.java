package com.jaywant.Service;

import com.jaywant.Model.AddEmployee;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeePasswordResetService {

  @Autowired
  private EmployeeEmailService emailService;

  @Autowired
  private EmployeeService employeeService;

  // Temporary in-memory storage for OTPs (email -> OTP)
  private final Map<String, String> otpStorage = new HashMap<>();

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  // Generate and send OTP for password reset.
  public void sendResetOTP(String email) {
    AddEmployee employee = employeeService.getEmployeeByEmail(email);
    if (employee == null) {
      throw new RuntimeException("Employee not found");
    }
    String otp = generateOTP();
    otpStorage.put(email, otp);
    boolean emailSent = emailService.sendEmail(otp, "Password Reset OTP", email);
    if (!emailSent) {
      throw new RuntimeException("Failed to send OTP email");
    }
  }

  // Generate a simple 6-character OTP.
  private String generateOTP() {
    return UUID.randomUUID().toString().substring(0, 6);
  }

  // Verify the OTP provided for the given email.
  public boolean verifyOTP(String email, String otp) {
    String storedOtp = otpStorage.get(email);
    if (storedOtp != null && storedOtp.equals(otp)) {
      otpStorage.remove(email);
      return true;
    }
    return false;
  }

  // Reset the employee's password using the new password.
  public void resetPassword(String email, String newPassword) {
    AddEmployee employee = employeeService.getEmployeeByEmail(email);
    if (employee == null) {
      throw new RuntimeException("Employee not found");
    }
    employee.setPassword(passwordEncoder.encode(newPassword));
    // Update using the employee's ID
    employeeService.updateEmployee(employee, employee.getEmpId());
  }
}
