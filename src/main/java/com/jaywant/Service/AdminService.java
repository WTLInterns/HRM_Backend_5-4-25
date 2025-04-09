package com.jaywant.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jaywant.Model.Admin;
import com.jaywant.Repo.AdminRepository;

@Service
public class AdminService {

  @Autowired
  private AdminRepository adminRepository;

  // Directory to save uploaded files.
  private final String uploadDir = "src/main/resources/upload/";

  // Register a new admin user.
  public Admin registerAdmin(Admin admin) {
    return adminRepository.save(admin);
  }

  // Simple login using email and password.
  public Optional<Admin> login(String email, String password) {
    Optional<Admin> adminOpt = adminRepository.findByEmail(email);
    if (adminOpt.isPresent() && adminOpt.get().getPassword().equals(password)) {
      return adminOpt;
    }
    return Optional.empty();
  }

  /**
   * Update admin details including updating the profile image if provided.
   *
   * @param admin          The Admin object containing updated data.
   * @param profileImgFile MultipartFile for the new profile image. May be null or
   *                       empty.
   * @return the updated Admin.
   */
  public Admin updateAdmin(Admin admin, MultipartFile profileImgFile) {
    Optional<Admin> existing = adminRepository.findById(admin.getId());
    if (existing.isPresent()) {
      Admin adminToUpdate = existing.get();

      // Update fields
      adminToUpdate.setName(admin.getName());
      adminToUpdate.setEmail(admin.getEmail());
      adminToUpdate.setPassword(admin.getPassword());
      adminToUpdate.setMobileno(admin.getMobileno());
      adminToUpdate.setRoll(admin.getRoll());

      // Handle file upload
      if (profileImgFile != null && !profileImgFile.isEmpty()) {
        String savedFileName = saveFile(profileImgFile);
        adminToUpdate.setProfileImg(savedFileName);
      }

      return adminRepository.save(adminToUpdate);
    } else {
      throw new RuntimeException("Admin not found");
    }
  }

  // Overload: update admin without a file upload
  public Admin updateAdmin(Admin admin) {
    return updateAdmin(admin, null);
  }

  // Find admin by email.
  public Optional<Admin> findByEmail(String email) {
    return adminRepository.findByEmail(email);
  }

  // Delete admin by id.
  public void deleteAdmin(Long id) {
    adminRepository.deleteById(id);
  }

  // Update admin password by id.
  public void updatePassword(Long id, String newPassword) {
    Optional<Admin> adminOpt = adminRepository.findById(id);
    if (adminOpt.isPresent()) {
      Admin admin = adminOpt.get();
      admin.setPassword(newPassword);
      adminRepository.save(admin);
    } else {
      throw new RuntimeException("Admin not found");
    }
  }

  // Utility method to save a MultipartFile to the upload directory.
  private String saveFile(MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
    String fileName = System.currentTimeMillis() + "_" + (originalFilename != null ? originalFilename : "file");
    try {
      Path uploadPath = Paths.get(uploadDir);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }
      Path filePath = uploadPath.resolve(fileName);
      Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to save file " + fileName);
    }
    return fileName;
  }
}
