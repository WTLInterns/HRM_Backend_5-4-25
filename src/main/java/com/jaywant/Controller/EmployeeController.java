package com.jaywant.Controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jaywant.Model.AddEmployee;
import com.jaywant.Service.EmployeeService;

@RestController
@RequestMapping("/employee")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {

  @Autowired
  private EmployeeService empService;

  /**
   * Endpoint for employee registration.
   * Expects an AddEmployee object in the request body.
   */
  @PostMapping("/register")
  public ResponseEntity<Map<String, Object>> registerEmployee(@RequestBody AddEmployee employee) {
    Map<String, Object> result = empService.registerEmployee(employee);
    if ("success".equals(result.get("status"))) {
      return ResponseEntity.status(HttpStatus.CREATED).body(result);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
  }

  /**
   * Endpoint for employee login.
   * Expects 'email' and 'password' as request parameters.
   */
  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> login(@RequestParam String email,
      @RequestParam String password) {
    Map<String, Object> response = empService.login(email, password);
    if (response.containsKey("message") && "Login successful".equals(response.get("message"))) {
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
  }
}
