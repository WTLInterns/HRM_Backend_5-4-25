package com.jaywant.DTO;

import com.jaywant.Model.Attendance;
import com.jaywant.Model.Employee;
import java.util.List;

public class EmployeeWithAttendanceDTO {
  private Employee employee;
  private List<Attendance> attendanceList;

  // Constructors
  public EmployeeWithAttendanceDTO() {
  }

  public EmployeeWithAttendanceDTO(Employee employee, List<Attendance> attendanceList) {
    this.employee = employee;
    this.attendanceList = attendanceList;
  }

  // Getters and Setters
  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public List<Attendance> getAttendanceList() {
    return attendanceList;
  }

  public void setAttendanceList(List<Attendance> attendanceList) {
    this.attendanceList = attendanceList;
  }
}
