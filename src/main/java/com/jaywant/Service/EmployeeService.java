package com.jaywant.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.jaywant.Model.AddEmployee;
import com.jaywant.Model.AddSubAdmin;
import com.jaywant.Model.Attendance;
import com.jaywant.Repo.AddEmployeeRepo;
import com.jaywant.Repo.AttendanceRepo;

@Service
public class EmployeeService {

    @Autowired
    private AddEmployeeRepo addEmpRepo; // Use AddEmployeeRepo for all operations

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeEmailService employeeEmailService;

    /**
     * Registers an employee and creates an AddEmployee record.
     * If no password is provided, a default password is generated as
     * "<firstName>@123".
     */
    public Map<String, Object> registerEmployee(@RequestBody AddEmployee employee) {
        Map<String, Object> response = new HashMap<>();

        // Generate default password if none provided
        if (employee.getPassword() == null || employee.getPassword().trim().isEmpty()) {
            String defaultPassword = employee.getFirstName().trim() + "@123";
            String encodedPassword = passwordEncoder.encode(defaultPassword);
            employee.setPassword(encodedPassword);
            response.put("defaultPassword", defaultPassword);
        } else {
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        }

        // Save the employee record
        AddEmployee savedEmployee = addEmpRepo.save(employee);

        // Optionally send a welcome email with the default password
        employeeEmailService.sendEmployeeCredentials(savedEmployee);

        response.put("user", savedEmployee);
        response.put("status", "success");
        return response;
    }

    /**
     * Authenticates an employee using their email and password.
     */
    public Map<String, Object> login(String email, String password) {
        Map<String, Object> response = new HashMap<>();
        AddEmployee emp = addEmpRepo.findByEmail(email);
        if (emp == null) {
            response.put("message", "Employee not found");
            return response;
        }
        if (passwordEncoder.matches(password, emp.getPassword())) {
            response.put("empId", emp.getEmpId());
            response.put("firstName", emp.getFirstName());
            response.put("lastName", emp.getLastName());
            response.put("email", emp.getEmail());
            response.put("phone", emp.getPhone());
            response.put("aadharNo", emp.getAadharNo());
            response.put("panCard", emp.getPanCard());
            response.put("education", emp.getEducation());
            response.put("bloodGroup", emp.getBloodGroup());
            response.put("jobRole", emp.getJobRole());
            response.put("gender", emp.getGender());
            response.put("address", emp.getAddress());
            response.put("birthDate", emp.getBirthDate());
            response.put("joiningDate", emp.getJoiningDate());
            response.put("status", emp.getStatus());
            response.put("bankName", emp.getBankName());
            response.put("bankAccountNo", emp.getBankAccountNo());
            response.put("bankIfscCode", emp.getBankIfscCode());
            response.put("branchName", emp.getBranchName());
            response.put("salary", emp.getSalary());
            response.put("company", emp.getCompany());
            response.put("roll", emp.getRoll());
            response.put("password", emp.getPassword()); // encoded password

            // SubAdmin Info (if linked)
            AddSubAdmin subAdmin = emp.getAddSubAdmin();
            if (subAdmin != null) {
                response.put("subAdminId", subAdmin.getId());
                response.put("subAdminName", subAdmin.getName() + " " + subAdmin.getLastname());
                response.put("subAdminEmail", subAdmin.getEmail());
                response.put("subAdminPhone", subAdmin.getPhoneno());
                response.put("registerCompanyName", subAdmin.getRegistercompanyname());
                response.put("companyLogo", subAdmin.getCompanylogo());
                response.put("signature", subAdmin.getSignature());
                response.put("stampImg", subAdmin.getStampImg());
                response.put("subAdminRole", subAdmin.getRole());
            } else {
                response.put("subAdmin", "Not Assigned");
            }
            response.put("message", "Login successful");
        } else {
            response.put("message", "Invalid credentials");
        }
        return response;
    }

    /**
     * Resets the password for an employee.
     */
    public AddEmployee forgotPassword(AddEmployee updateUser, String email) {
        AddEmployee emp = addEmpRepo.findByEmail(email);
        if (emp == null) {
            throw new RuntimeException("Employee with email " + email + " not found.");
        }
        emp.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        return addEmpRepo.save(emp);
    }

    /**
     * Retrieves an employee by email.
     */
    public AddEmployee profile(String email) {
        return addEmpRepo.findByEmail(email);
    }

    // -----------------------------
    // Operations on Employee Records
    // -----------------------------

    public AddEmployee addEmp(AddEmployee addEmp) {
        // Auto-generate default password if none is provided.
        if (addEmp.getPassword() == null || addEmp.getPassword().trim().isEmpty()) {
            String defaultPassword = addEmp.getFirstName().trim() + "@123";
            addEmp.setPassword(passwordEncoder.encode(defaultPassword));
        } else {
            addEmp.setPassword(passwordEncoder.encode(addEmp.getPassword()));
        }
        return addEmpRepo.save(addEmp);
    }

    public List<AddEmployee> getAllEmployee() {
        return addEmpRepo.findAll();
    }

    public List<AddEmployee> getEmployeesByCompany(String company) {
        return addEmpRepo.findByCompany(company);
    }

    public void addEmployeeDelete(int empId) {
        addEmpRepo.deleteById(empId);
    }

    public AddEmployee getEmployeeById(int empId) {
        return addEmpRepo.findById(empId);
    }

    /**
     * Deletes all attendance records associated with an employee, then deletes the
     * employee record.
     */
    public void deleteEmpId(int empId) {
        List<Attendance> attendances = attendanceRepo.findByEmployeeEmpId(empId);
        for (Attendance att : attendances) {
            attendanceRepo.delete(att);
        }
        addEmpRepo.deleteById(empId);
    }

    public AddEmployee updateEmployee(AddEmployee addEmp, int empId) {
        addEmp.setEmpId(empId);
        return addEmpRepo.save(addEmp);
    }

    /**
     * Finds an employee record by full name.
     * Splits a name like "Pranav Paul" into firstName and lastName (both trimmed).
     */
    public AddEmployee findByEmployeeName(String name) {
        String[] parts = name.trim().split("\\s+");
        AddEmployee employee = null;
        if (parts.length >= 2) {
            // Trim each part individually.
            String firstName = parts[0].trim();
            String lastName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)).trim();
            employee = addEmpRepo.findByFirstNameAndLastName(firstName, lastName);
            if (employee == null) {
                employee = addEmpRepo.findByFirstName(firstName);
            }
        } else {
            employee = addEmpRepo.findByFirstName(name.trim());
        }
        if (employee == null) {
            throw new IllegalArgumentException("No employee found with name: " + name);
        }
        return employee;
    }

    public AddEmployee getEmployeeByEmail(String email) {
        return addEmpRepo.findByEmail(email);
    }
}
