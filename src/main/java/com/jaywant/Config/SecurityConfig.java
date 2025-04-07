package com.jaywant.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jaywant.Jwt.JwtAuthFilter;
import com.jaywant.Service.EmployeeDetailsServiceImpl;

@Configuration
public class SecurityConfig {

	@Autowired
	private EmployeeDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// Disable CSRF for REST APIs or testing purposes.
				.csrf(AbstractHttpConfigurer::disable)
				// Enable CORS if needed.
				.cors(Customizer.withDefaults())
				// Set session management to stateless.
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// Set authorization rules.
				.authorizeHttpRequests(auth -> auth
						// Permit all requests for these endpoints (for testing/Postman)
						.requestMatchers("/auth/**", "/public/**", "/api/subadmin/**").permitAll()
						// Other endpoints with roles
						.requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
						.requestMatchers("/emp/**").hasAnyAuthority("EMPLOYEE")
						.requestMatchers("/adminuser/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
						// All other requests require authentication.
						.anyRequest().authenticated())
				// Add the custom authentication provider.
				.authenticationProvider(authenticationProvider())
				// Add the JWT filter before the UsernamePasswordAuthenticationFilter.
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImpl);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
