// package com.jaywant.Controller;

// import java.time.LocalDateTime;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.bind.annotation.*;

// import com.jaywant.DTO.LoginRequest;
// import com.jaywant.Model.Employee;
// import com.jaywant.Service.EmailService;
// import com.jaywant.Service.EmployeeService;

// @RestController
// @RequestMapping("/auth")
// @CrossOrigin(origins = "http://localhost:3000")
// public class AuthController {

// 	@Autowired
// 	private EmployeeService empService;

// 	@Autowired
// 	private EmailService emailService;

// 	// Register any user (Admin/SubAdmin/Employee)
// 	@PostMapping("/register")
// 	public ResponseEntity<Map<String, Object>> createUser(@RequestBody Employee user) {
// 		Map<String, Object> result = empService.register(user);
// 		if (result.get("status").equals("success")) {
// 			return ResponseEntity.status(HttpStatus.CREATED).body(result);
// 		} else {
// 			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
// 		}
// 	}

// 	// Login
// 	@PostMapping("/login")
// 	public ResponseEntity<?> login(@RequestBody LoginRequest req) {
// 		Map<String, Object> loginResponse = empService.login(req.getEmail(), req.getPassword());

// 		if (loginResponse != null && "Login successful".equals(loginResponse.get("message"))) {
// 			String email = (String) loginResponse.get("email"); // Adjust key name as per your map
// 			String subject = "Login Notification";
// 			String message = "You have logged in successfully.\nEmail: " + email +
// 					"\nTime: " + LocalDateTime.now();

// 			try {
// 				// emailService.sendHtmlEmail(message, subject, email); // Uncomment if needed
// 				return ResponseEntity.ok(loginResponse);
// 			} catch (Exception e) {
// 				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login success, but email failed.");
// 			}
// 		} else {
// 			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
// 		}
// 	}

// 	// Reset password (only for logged-in users)
// 	@PutMapping("/reset")
// 	public ResponseEntity<?> resetPassword(@RequestBody Employee updateUser) {
// 		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
// 		String email = authentication.getName();

// 		Employee updated = empService.forgotPassword(updateUser, email);
// 		if (updated != null) {
// 			return ResponseEntity.ok(updated);
// 		} else {
// 			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Reset failed");
// 		}
// 	}

// 	// Profile info
// 	@GetMapping("/profile")
// 	public ResponseEntity<Employee> showProfile() {
// 		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
// 		String email = authentication.getName();
// 		Employee user = empService.profile(email);

// 		if (user != null) {
// 			return ResponseEntity.ok(user);
// 		} else {
// 			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
// 		}
// 	}
// }

package com.jaywant.Controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.jaywant.DTO.LoginRequest;
import com.jaywant.Model.AddEmployee;
import com.jaywant.Service.EmailService;
import com.jaywant.Service.EmployeeService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

	@Autowired
	private EmployeeService empService;

	@Autowired
	private EmailService emailService;

	// Register any user (Admin/SubAdmin/Employee) using AddEmployee
	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> createUser(@RequestBody AddEmployee user) {
		Map<String, Object> result = empService.registerEmployee(user);
		if ("success".equals(result.get("status"))) {
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}

	// Login endpoint using email and password (without JWT)
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest req) {
		Map<String, Object> loginResponse = empService.login(req.getEmail(), req.getPassword());
		if (loginResponse != null && "Login successful".equals(loginResponse.get("message"))) {
			String email = (String) loginResponse.get("email");
			String subject = "Login Notification";
			String message = "You have logged in successfully.\nEmail: " + email +
					"\nTime: " + LocalDateTime.now();
			try {
				// Uncomment below line if you want to send an email notification
				// emailService.sendHtmlEmail(message, subject, email);
				return ResponseEntity.ok(loginResponse);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Login successful, but email failed.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
	}

	// Reset password (requires the user to be already authenticated)
	@PutMapping("/reset")
	public ResponseEntity<?> resetPassword(@RequestBody AddEmployee updateUser) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		AddEmployee updated = empService.forgotPassword(updateUser, email);
		if (updated != null) {
			return ResponseEntity.ok(updated);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Reset failed");
		}
	}

	// Profile info endpoint
	@GetMapping("/profile")
	public ResponseEntity<AddEmployee> showProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		AddEmployee user = empService.profile(email);
		if (user != null) {
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}
