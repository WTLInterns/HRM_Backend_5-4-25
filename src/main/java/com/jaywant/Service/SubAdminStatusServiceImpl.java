package com.jaywant.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaywant.Model.AddSubAdmin;
import com.jaywant.Repo.AddSubAdminRepository;

@Service
public class SubAdminStatusServiceImpl implements SubAdminStatusService {

  @Autowired
  private AddSubAdminRepository subAdminRepo;

  @Override
  public List<AddSubAdmin> getSubAdminsByStatus(String status) {
    // Status should be "0" or "1"
    return subAdminRepo.findByStatus(status);
  }

  @Override
  public AddSubAdmin updateSubAdminStatus(int id, String status) {
    Optional<AddSubAdmin> optionalSubAdmin = subAdminRepo.findById(id);
    if (!optionalSubAdmin.isPresent()) {
      throw new RuntimeException("SubAdmin with id " + id + " not found");
    }
    AddSubAdmin subAdmin = optionalSubAdmin.get();

    // Validate the status value (Optional, you may add more robust validation)
    if (!"0".equals(status) && !"1".equals(status)) {
      throw new RuntimeException("Invalid status. Use \"0\" for inactive or \"1\" for active.");
    }

    subAdmin.setStatus(status);
    return subAdminRepo.save(subAdmin);
  }
}
