package com.jaywant.Service;

import com.jaywant.Model.AddSubAdmin;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubAdminPasswordResetService {

  @Autowired
  private EmailService emailService;

  @Autowired
  private AddSubAdminService subAdminService;

  // Temporary in-memory storage for OTPs (email -> OTP)
  private final Map<String, String> otpStorage = new HashMap<>();

  // Generate and send OTP to the provided email
  public void sendResetOTP(String email) {
    Optional<AddSubAdmin> subAdminOpt = subAdminService.getSubAdminByEmail(email);
    if (!subAdminOpt.isPresent()) {
      throw new RuntimeException("SubAdmin not found");
    }
    String otp = generateOTP();
    otpStorage.put(email, otp); // Store OTP in memory

    // Send the OTP using the existing EmailService.
    boolean emailSent = emailService.sendHtmlEmail(otp, "Password Reset OTP", email);
    if (!emailSent) {
      throw new RuntimeException("Failed to send OTP email");
    }
  }

  // Generate a simple 6-character OTP
  private String generateOTP() {
    return UUID.randomUUID().toString().substring(0, 6);
  }

  // Verify the provided OTP against the stored OTP
  public boolean verifyOTP(String email, String otp) {
    String storedOtp = otpStorage.get(email);
    if (storedOtp != null && storedOtp.equals(otp)) {
      otpStorage.remove(email); // Clear OTP after successful validation
      return true;
    }
    return false;
  }

  // Reset the password for the sub-admin identified by email.
  public void resetPassword(String email, String newPassword) {
    Optional<AddSubAdmin> subAdminOpt = subAdminService.getSubAdminByEmail(email);
    if (!subAdminOpt.isPresent()) {
      throw new RuntimeException("SubAdmin not found");
    }
    subAdminService.updatePassword(subAdminOpt.get().getId(), newPassword);
  }
}
