package com.jaywant.Service;

import com.jaywant.DTO.LoginRequest;
import com.jaywant.Jwt.JwtUtils;
import com.jaywant.Model.AddEmployee;
import com.jaywant.Model.Attendance;
import com.jaywant.Model.Employee;
import com.jaywant.Repo.AddEmployeeRepo;
import com.jaywant.Repo.AttendanceRepo;
import com.jaywant.Repo.EmployeeRepo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo empRepo; // Used for authentication/profile

    @Autowired
    private AddEmployeeRepo addEmpRepo; // Used for employee records

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registration for authentication (Employee entity)
    public Map<String, Object> register(Employee user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Employee savedUser = empRepo.save(user);
        String jwtToken = jwtUtils.generateToken(savedUser);
        Map<String, Object> response = new HashMap<>();
        response.put("user", savedUser);
        response.put("jwtToken", jwtToken);
        return response;
    }

    // Login method using Employee entity
    public LoginRequest login(LoginRequest loginRequest) {
        LoginRequest response = new LoginRequest();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            Employee emp = empRepo.findByEmail(loginRequest.getEmail());
            String jwt = jwtUtils.generateToken(emp);
            response.setToken(jwt);
            response.setEmail(emp.getEmail());
            response.setPassword(emp.getPassword());
            response.setRole(emp.getRole());
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return response;
    }

    // Reset password for Employee entity
    public Employee forgotPassword(Employee updateUser, String email) {
        Employee emp = empRepo.findByEmail(email);
        if (emp == null) {
            throw new RuntimeException("Employee with email " + email + " not found.");
        }
        emp.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        emp = empRepo.save(emp);
        return emp;
    }

    // Get employee profile using Employee entity
    public Employee profile(String email) {
        return empRepo.findByEmail(email);
    }

    // -----------------------------
    // Operations on Employee Records (AddEmployee)
    // -----------------------------

    // Add a new employee record (caller must set the company field correctly)
    public AddEmployee addEmp(AddEmployee addEmp) {
        return addEmpRepo.save(addEmp);
    }

    // Get all employee records
    public List<AddEmployee> getAllEmployee() {
        return addEmpRepo.findAll();
    }

    // Get employees filtered by company (for sub-admin access)
    public List<AddEmployee> getEmployeesByCompany(String company) {
        return addEmpRepo.findByCompany(company);
    }

    // Delete employee record by ID
    public void addEmployeeDelete(int empId) {
        addEmpRepo.deleteById(empId);
    }

    // Get employee record by ID
    public AddEmployee getEmployeeById(int empId) {
        // If you have a custom method declared in AddEmployeeRepo that returns
        // AddEmployee, use that.
        // Otherwise, if using the standard Optional, modify as follows:
        // return addEmpRepo.findById(empId).orElse(null);
        return addEmpRepo.findById(empId);
    }

    // Delete attendance records first then delete the employee record
    public void deleteEmpId(int empId) {
        List<Attendance> attendances = attendanceRepo.findByEmployeeEmpId(empId);
        for (Attendance att : attendances) {
            attendanceRepo.delete(att);
        }
        addEmpRepo.deleteById(empId);
    }

    // Update an existing employee record
    public AddEmployee updateEmployee(AddEmployee addEmp, int empId) {
        addEmp.setEmpId(empId);
        return addEmpRepo.save(addEmp);
    }

    // Find employee record by name (searches using first name and optionally last
    // name)
    public AddEmployee findByEmployeeName(String name) {
        String[] parts = name.trim().split("\\s+");
        AddEmployee employee = null;
        if (parts.length >= 2) {
            String firstName = parts[0];
            String lastName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
            employee = addEmpRepo.findByFirstNameAndLastName(firstName, lastName);
            if (employee == null) {
                employee = addEmpRepo.findByFirstName(firstName);
            }
        } else {
            employee = addEmpRepo.findByFirstName(name);
        }
        if (employee == null) {
            throw new IllegalArgumentException("No employee found with name: " + name);
        }
        return employee;
    }

    // Get employee record by email
    public AddEmployee getEmployeeByEmail(String email) {
        return addEmpRepo.findByEmail(email);
    }
}
