package com.jaywant.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jaywant.Model.AddSubAdmin;

@Service
public class SubAdminEmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  public void sendSubAdminCredentials(AddSubAdmin subAdmin) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(subAdmin.getEmail());
    message.setSubject("🎉 Welcome to WTL Tourism Pvt. Ltd. - Your HRM SubAdmin Access");

    String fullName = capitalize(subAdmin.getName()) + " " + capitalize(subAdmin.getLastname());

    String body = ""
        + "Hello " + fullName + ",\n\n"
        + "🎉 Welcome to WTL Tourism Pvt. Ltd.!\n\n"
        + "We're excited to have you on board as a HRM-Sub-Admin. Your account has been created successfully. Below are your login credentials:\n\n"
      
        + "📧 Email: " + subAdmin.getEmail() + "\n"
        + "🔐 Password: " + subAdmin.getPassword() + "\n\n"
        + "👉 Click here to log in: http://localhost:5173/login\n\n"
        + "⚠️ Please change your password after your first login to ensure security.\n\n"
        + "If you face any issues, feel free to contact our support team.\n\n"
        + "Thanks & Regards,\n"
        + "Team WTL Tourism Pvt. Ltd.";

    message.setText(body);
    javaMailSender.send(message);
  }

  public void sendSubAdminNotification(AddSubAdmin subAdmin) {
    subAdmin.setPassword("subAdmin@123");
    sendSubAdminCredentials(subAdmin);
  }

  private String capitalize(String str) {
    if (str == null || str.isEmpty())
      return str;
    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }
}
