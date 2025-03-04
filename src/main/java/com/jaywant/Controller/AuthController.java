package com.jaywant.Controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaywant.DTO.LoginRequest;
import com.jaywant.Model.Employee;
import com.jaywant.Service.EmailService;
import com.jaywant.Service.EmployeeService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private EmployeeService empService;
	
	@Autowired
	private EmailService emailService;

	
	@PostMapping("/register")
	public Map<String, Object> createUser(@RequestBody Employee user) {
		return this.empService.register(user);
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginRequest> Login(@RequestBody LoginRequest req){
//		return ResponseEntity.ok(empService.login(req));
		LoginRequest loginRequest = this.empService.login(req);
		 if (loginRequest != null) {
		    	
		        String subject = "Login Content";
		        String message = "You have been logged in " + loginRequest.getEmail() + " " + LocalDateTime.now();
		        
		        try {
		            boolean emailSent = this.emailService.sendHtmlEmail(message, subject, loginRequest.getEmail());
		            
		            if (emailSent) {
		                return ResponseEntity.ok(loginRequest);  
		            } else {
		                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                                     .body(loginRequest);  
		            }
		        } catch (Exception e) {
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                                 .body(loginRequest);  
		        }
		        
		    } else {
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		    }
	}
	
	@PutMapping("/reset")
	public Employee forgotPassword(@RequestBody Employee updateUser) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String email = authentication.getName();

	    return this.empService.forgotPassword(updateUser, email);
	}
	
	
	 @GetMapping("/profile")
	    public Employee showProfile() {
	    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	            String email = authentication.getName();
	            Employee user = this.empService.profile(email);
	            return user;
	    }

}
