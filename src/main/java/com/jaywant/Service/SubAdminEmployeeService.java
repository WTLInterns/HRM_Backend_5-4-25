package com.jaywant.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jaywant.Model.AddEmployee;
import com.jaywant.Model.Attendance;
import com.jaywant.Repo.AddEmployeeRepo;
import com.jaywant.Repo.AttendanceRepo;
import com.jaywant.Repo.SubAdminEmployeeRepo;

@Service
public class SubAdminEmployeeService {

  @Autowired
  private AddEmployeeRepo addEmployeeRepo;

  @Autowired
  private SubAdminEmployeeRepo subAdminEmployeeRepo;

  @Autowired
  private AttendanceRepo attendanceRepo;

  // Helper method to split full name into first and last names.
  private String[] splitFullName(String fullName) {
    String[] parts = fullName.trim().split("\\s+");
    String firstName = parts[0];
    String lastName = "";
    if (parts.length > 1) {
      lastName = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length));
    }
    return new String[] { firstName, lastName };
  }

  public AddEmployee addEmployeeForCompany(AddEmployee employee, String registerCompanyName) {
    employee.setCompany(registerCompanyName);
    return addEmployeeRepo.save(employee);
  }

  public List<AddEmployee> getAllEmployeesByCompany(String registerCompanyName) {
    return addEmployeeRepo.findByCompany(registerCompanyName);
  }

  public AddEmployee getEmployeeByFullNameAndCompany(String employeeFullName, String registerCompanyName) {
    String[] names = splitFullName(employeeFullName);
    String firstName = names[0];
    String lastName = names[1];
    AddEmployee employee = null;
    if (!lastName.isEmpty()) {
      employee = subAdminEmployeeRepo.findByFirstNameAndLastNameAndCompany(firstName, lastName, registerCompanyName);
    } else {
      employee = addEmployeeRepo.findByFirstName(firstName);
      if (employee != null && !employee.getCompany().equalsIgnoreCase(registerCompanyName)) {
        employee = null;
      }
    }
    if (employee == null) {
      throw new RuntimeException(
          "Employee with name " + employeeFullName + " not found in company " + registerCompanyName);
    }
    return employee;
  }

  public AddEmployee updateEmployeeForCompany(AddEmployee employeeUpdates, String registerCompanyName,
      String employeeFullName) {
    AddEmployee existingEmployee = getEmployeeByFullNameAndCompany(employeeFullName, registerCompanyName);
    employeeUpdates.setEmpId(existingEmployee.getEmpId());
    employeeUpdates.setCompany(registerCompanyName);
    return addEmployeeRepo.save(employeeUpdates);
  }

  public void deleteEmployeeForCompany(String employeeFullName, String registerCompanyName) {
    AddEmployee employee = getEmployeeByFullNameAndCompany(employeeFullName, registerCompanyName);
    List<Attendance> attendanceList = attendanceRepo.findByEmployeeEmpId(employee.getEmpId());
    for (Attendance a : attendanceList) {
      attendanceRepo.delete(a);
    }
    addEmployeeRepo.deleteById(employee.getEmpId());
  }

  public Attendance addAttendanceForEmployee(Attendance attendance, String registerCompanyName,
      String employeeFullName) {
    AddEmployee employee = getEmployeeByFullNameAndCompany(employeeFullName, registerCompanyName);
    attendance.setEmployee(employee);
    return attendanceRepo.save(attendance);
  }

  public List<Attendance> getAttendanceByEmployee(String registerCompanyName, String employeeFullName) {
    AddEmployee employee = getEmployeeByFullNameAndCompany(employeeFullName, registerCompanyName);
    return attendanceRepo.findByEmployeeEmpId(employee.getEmpId());
  }

  public Attendance updateAttendanceStatusForEmployee(Long attendanceId, String newStatus, String registerCompanyName,
      String employeeFullName) {
    Attendance attendance = attendanceRepo.findById(attendanceId)
        .orElseThrow(() -> new RuntimeException("Attendance record not found with id: " + attendanceId));
    AddEmployee employee = getEmployeeByFullNameAndCompany(employeeFullName, registerCompanyName);
    if (attendance.getEmployee() == null || attendance.getEmployee().getEmpId() != employee.getEmpId()) {
      throw new RuntimeException(
          "Attendance record does not belong to employee " + employeeFullName + " from company " + registerCompanyName);
    }
    attendance.setStatus(newStatus);
    return attendanceRepo.save(attendance);
  }
}
