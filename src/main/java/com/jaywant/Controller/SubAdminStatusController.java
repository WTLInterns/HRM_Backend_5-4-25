package com.jaywant.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jaywant.Model.AddSubAdmin;
import com.jaywant.Service.SubAdminStatusService;

@RestController
@RequestMapping("/api/subadmin/status")
@CrossOrigin
public class SubAdminStatusController {

  @Autowired
  private SubAdminStatusService statusService;

  /**
   * Get all sub-admins with the given status.
   * Example: GET /api/subadmin/status/1 returns all active sub-admins.
   */
  @GetMapping("/{status}")
  public ResponseEntity<List<AddSubAdmin>> getSubAdminsByStatus(@PathVariable("status") String status) {
    try {
      List<AddSubAdmin> subAdmins = statusService.getSubAdminsByStatus(status);
      if (subAdmins.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
      return ResponseEntity.ok(subAdmins);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Update the status of a sub-admin.
   * Example: PUT /api/subadmin/status/5?status=1 sets the sub-admin with id 5 as
   * active.
   */
  @PutMapping("/{id}")
  public ResponseEntity<AddSubAdmin> updateSubAdminStatus(@PathVariable("id") int id,
      @RequestParam("status") String status) {
    try {
      AddSubAdmin updatedSubAdmin = statusService.updateSubAdminStatus(id, status);
      return ResponseEntity.ok(updatedSubAdmin);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }
}
