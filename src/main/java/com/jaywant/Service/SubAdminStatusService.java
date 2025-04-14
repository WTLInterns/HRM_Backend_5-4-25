package com.jaywant.Service;

import java.util.List;
import com.jaywant.Model.AddSubAdmin;

public interface SubAdminStatusService {
  /**
   * Retrieves all SubAdmin entities with the given status.
   * 
   * @param status "0" for inactive, "1" for active.
   * @return list of sub-admins.
   */
  List<AddSubAdmin> getSubAdminsByStatus(String status);

  /**
   * Updates the status of a SubAdmin.
   * 
   * @param id     The sub-admin id.
   * @param status "0" for inactive, "1" for active.
   * @return the updated sub-admin.
   */
  AddSubAdmin updateSubAdminStatus(int id, String status);
}
