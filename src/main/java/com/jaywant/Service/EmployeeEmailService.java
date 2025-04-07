package com.jaywant.Service;

import com.jaywant.Model.AddEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmployeeEmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  // Sends welcome email with login credentials to the employee.
  public void sendEmployeeCredentials(AddEmployee employee) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(employee.getEmail());
    message.setSubject("Welcome to Your Company - Employee Account Credentials");

    String defaultPassword = employee.getFirstName() + "@123";
    String body = ""
        + "Hello " + employee.getFirstName() + ",\n\n"
        + "Welcome to " + employee.getCompany() + "!\n\n"
        + "Your account has been created successfully. Below are your login credentials:\n\n"
        + "🆔 Employee ID: " + employee.getEmpId() + "\n"
        + "📧 Email: " + employee.getEmail() + "\n"
        + "🔐 Password: " + defaultPassword + "\n\n"
        + "Please log in and change your password after your first login for security purposes.\n\n"
        + "Thanks & Regards,\n"
        + "HR Team";
    message.setText(body);
    javaMailSender.send(message);
  }

  // Sends an OTP email for employee password reset.
  public boolean sendEmail(String otp, String subject, String toEmail) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(toEmail);
      message.setSubject(subject);
      String body = ""
          + "Hello,\n\n"
          + "Your password reset OTP is: " + otp + "\n\n"
          + "Please use this OTP to reset your password.\n\n"
          + "Thanks & Regards,\n"
          + "HR Team";
      message.setText(body);
      javaMailSender.send(message);
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
}
