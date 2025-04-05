package com.jaywant.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.jaywant.DTO.LoginRequest;
import com.jaywant.Jwt.JwtUtils;
import com.jaywant.Model.AddEmployee;
import com.jaywant.Model.Attendance;
import com.jaywant.Model.Employee;
import com.jaywant.Repo.AddEmployeeRepo;
import com.jaywant.Repo.AttendanceRepo;
import com.jaywant.Repo.EmployeeRepo;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo empRepo;
    
    @Autowired
    private AddEmployeeRepo addEmpRepo;
    
    @Autowired
    private AttendanceRepo attendanceRepo;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Map<String, Object> register(Employee user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Employee savedUser = empRepo.save(user);
        String jwtToken = jwtUtils.generateToken(savedUser);
        Map<String, Object> response = new HashMap<>();
        response.put("user", savedUser);
        response.put("jwtToken", jwtToken);
        return response;
    }
    
    public LoginRequest login(LoginRequest loginRequest) {
        LoginRequest response = new LoginRequest();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var emp = this.empRepo.findByEmail(loginRequest.getEmail());
            var jwt = jwtUtils.generateToken(emp);
            response.setToken(jwt);
            response.setEmail(emp.getEmail());
            response.setPassword(emp.getPassword());
            response.setRole(emp.getRole());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return response;
    }
    
    public Employee forgotPassword(Employee updateUser, String email) {
        Employee emp = this.empRepo.findByEmail(email);
        if (emp == null) {
            throw new RuntimeException("Employee with email " + email + " not found.");
        }
        emp.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        emp = this.empRepo.save(emp);
        return emp;
    }
    
    public Employee profile(String email) {
        Employee emp = this.empRepo.findByEmail(email);
        return emp;
    }
    
    public AddEmployee addEmp(AddEmployee addEmp) {
        return this.addEmpRepo.save(addEmp);
    }
    
    public List<AddEmployee> getAllEmployee(){
        return this.addEmpRepo.findAll();
    }
    
    public void addEmployeeDelete(int empId) {
        this.addEmpRepo.deleteById(empId);
    }
    
    public AddEmployee getEmployeeById(int empId) {
        return this.addEmpRepo.findById(empId);
    }
    
    // Delete attendance records first before removing the employee.
    public void deleteEmpId(int empId) {
        List<Attendance> attendances = attendanceRepo.findByEmployeeEmpId(empId);
        for (Attendance att : attendances) {
            attendanceRepo.delete(att);
        }
        addEmpRepo.deleteById(empId);
    }
    
    public AddEmployee updateEmployee(AddEmployee addEmp, int empId) {
        addEmp.setEmpId(empId);
        return this.addEmpRepo.save(addEmp);
    }
    
    public AddEmployee findByEmployeeName(String name) {
        String[] parts = name.trim().split("\\s+");
        AddEmployee employee = null;
        if (parts.length >= 2) {
            String firstName = parts[0];
            String lastName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
            employee = addEmpRepo.findByFirstNameAndLastName(firstName, lastName);
            // Fallback to first name only if full-name lookup returns null.
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


}
