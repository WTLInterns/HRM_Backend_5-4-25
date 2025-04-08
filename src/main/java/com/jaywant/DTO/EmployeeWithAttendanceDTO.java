package com.jaywant.DTO;

import com.jaywant.Model.Attendance;
import com.jaywant.Model.AddEmployee;
import java.util.List;

public class EmployeeWithAttendanceDTO {
  private AddEmployee employee;
  private List<Attendance> attendanceList;

  // Constructors
  public EmployeeWithAttendanceDTO() {
  }

  public EmployeeWithAttendanceDTO(AddEmployee employee, List<Attendance> attendanceList) {
    this.employee = employee;
    this.attendanceList = attendanceList;
  }

  // Getters and Setters
  public AddEmployee getEmployee() {
    return employee;
  }

  public void setEmployee(AddEmployee employee) {
    this.employee = employee;
  }

  public List<Attendance> getAttendanceList() {
    return attendanceList;
  }

  public void setAttendanceList(List<Attendance> attendanceList) {
    this.attendanceList = attendanceList;
  }
}
