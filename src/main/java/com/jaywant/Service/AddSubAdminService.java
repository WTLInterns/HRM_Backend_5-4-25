package com.jaywant.Service;

import com.jaywant.DTO.EmployeeWithAttendanceDTO;
import com.jaywant.Model.AddSubAdmin;
import com.jaywant.Model.Attendance;
import com.jaywant.Model.Employee;
import com.jaywant.Repo.AddSubAdminRepository;
import com.jaywant.Repo.AttendanceRepo;
import com.jaywant.Repo.EmployeeRepo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AddSubAdminService {

  @Autowired
  private AddSubAdminRepository repo;

  @Autowired
  private SubAdminEmailService mailService;

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  // Updated upload directory.
  private final String uploadDir = "src/main/resources/upload/";

  public AddSubAdmin createSubAdmin(AddSubAdmin subAdmin, MultipartFile stampImgFile, MultipartFile signatureFile,
      MultipartFile logoFile) {
    // Check if subadmin with provided email already exists.
    Optional<AddSubAdmin> existingSubAdmin = repo.findByEmail(subAdmin.getEmail());
    if (existingSubAdmin.isPresent()) {
      throw new RuntimeException("SubAdmin with email " + subAdmin.getEmail() + " already exists.");
    }

    // Save the stamp image file (company logo) if provided.
    if (stampImgFile != null && !stampImgFile.isEmpty()) {
      String stampImgFileName = saveFile(stampImgFile);
      subAdmin.setStampImg(stampImgFileName);
    } else {
      subAdmin.setStampImg(null);
    }

    // Save the signature file if provided.
    if (signatureFile != null && !signatureFile.isEmpty()) {
      String signatureFileName = saveFile(signatureFile);
      subAdmin.setSignature(signatureFileName);
    } else {
      subAdmin.setSignature(null);
    }

    if (logoFile != null && !logoFile.isEmpty()) {
      String logoFileName = saveFile(logoFile);
      subAdmin.setCompanylogo(logoFileName);
    } else {
      subAdmin.setCompanylogo(null);
    }

    // Set default password and encode it.
    String rawPassword = "subAdmin@123";
    String encodedPassword = passwordEncoder.encode(rawPassword);
    subAdmin.setPassword(encodedPassword);

    // Save sub-admin details to the database.
    AddSubAdmin saved = repo.save(subAdmin);
    return saved;
  }

  // Utility method to save a file to the upload directory.
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
    }
    return fileName;
  }

  public AddSubAdmin login(String email, String password) {
    Optional<AddSubAdmin> subAdminOpt = repo.findByEmail(email);
    if (subAdminOpt.isPresent()) {
      AddSubAdmin subAdmin = subAdminOpt.get();
      if (passwordEncoder.matches(password, subAdmin.getPassword())) {
        return subAdmin;
      }
    }
    return null;
  }

  public void updatePassword(int id, String newPassword) {
    AddSubAdmin subAdmin = repo.findById(id).orElseThrow();
    subAdmin.setPassword(passwordEncoder.encode(newPassword));
    repo.save(subAdmin);
  }

  // New method to manually trigger email sending for a sub-admin.
  public boolean sendSubAdminEmail(String email) {
    Optional<AddSubAdmin> subAdminOpt = repo.findByEmail(email);
    if (subAdminOpt.isPresent()) {
      AddSubAdmin subAdmin = subAdminOpt.get();
      String defaultPassword = "subAdmin@123";
      subAdmin.setPassword(defaultPassword); // So plain password is passed in email
      mailService.sendSubAdminCredentials(subAdmin);

      return true;
    }
    return false;
  }

  // New method to retrieve all sub-admin information.
  public List<AddSubAdmin> getAllSubAdmins() {
    return repo.findAll();
  }

  // New method to retrieve a specific sub-admin by email.
  public Optional<AddSubAdmin> getSubAdminByEmail(String email) {
    return repo.findByEmail(email);
  }

  // New method to retrieve a specific sub-admin by ID.
  public Optional<AddSubAdmin> getSubAdminById(int id) {
    return repo.findById(id);
  }

  @Autowired
  private EmployeeRepo employeeRepo;

  @Autowired
  private AttendanceRepo attendanceRepo;

  public List<EmployeeWithAttendanceDTO> getEmployeesWithAttendanceByCompany(String companyName) {
    List<Employee> employees = employeeRepo.findByCompany(companyName);
    List<EmployeeWithAttendanceDTO> result = new ArrayList<>();

    for (Employee emp : employees) {
      List<Attendance> attendanceList = attendanceRepo.findByEmployeeEmpId(emp.getEmpId());

      result.add(new EmployeeWithAttendanceDTO(emp, attendanceList));
    }

    return result;
  }

  // New method to delete a sub-admin by ID.
  public void deleteSubAdmin(int id) {
    repo.deleteById(id);
  }
}
