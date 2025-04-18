// package com.jaywant.Config;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import com.jaywant.Jwt.JwtAuthFilter;
// import com.jaywant.Service.EmployeeDetailsServiceImpl;

// @Configuration
// public class SecurityConfig {

// 	@Autowired
// 	private EmployeeDetailsServiceImpl userDetailsServiceImpl;

// 	@Autowired
// 	private JwtAuthFilter jwtAuthFilter;

// 	@Bean
// 	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
// 		http
// 				// Disable CSRF for REST APIs or testing purposes.
// 				.csrf(AbstractHttpConfigurer::disable)
// 				// Enable CORS if needed.
// 				.cors(Customizer.withDefaults())
// 				// Set session management to stateless.
// 				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
// 				// Set authorization rules.
// 				.authorizeHttpRequests(auth -> auth
// 						// Permit public endpoints
// 						.requestMatchers(
// 								"/employee/register",
// 								"/employee/login",
// 								"/employee/send-email/employee",
// 								"/employee/forgot-password/**")
// 						.permitAll()

// 						// Already whitelisted
// 						.requestMatchers("/auth/**", "/public/**", "/api/subadmin/**").permitAll()

// 						// Role-based access
// 						.requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
// 						.requestMatchers("/emp/**").hasAnyAuthority("EMPLOYEE")
// 						.requestMatchers("/adminuser/**").hasAnyAuthority("ADMIN", "EMPLOYEE")

// 						// All others require auth
// 						.anyRequest().authenticated())

// 				// Add the custom authentication provider.
// 				.authenticationProvider(authenticationProvider())
// 				// Add the JWT filter before the UsernamePasswordAuthenticationFilter.
// 				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

// 		return http.build();
// 	}

// 	@Bean
// 	public PasswordEncoder passwordEncoder() {
// 		return new BCryptPasswordEncoder();
// 	}

// 	@Bean
// 	public AuthenticationProvider authenticationProvider() {
// 		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
// 		daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImpl);
// 		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
// 		return daoAuthenticationProvider;
// 	}

// 	@Bean
// 	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
// 		return config.getAuthenticationManager();
// 	}
// }

package com.jaywant.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.jaywant.Service.EmployeeDetailsServiceImpl;

@Configuration
public class SecurityConfig {

	@Autowired
	private EmployeeDetailsServiceImpl userDetailsService;

	@Bean
	public UserDetailsService userDetailsService() {
		return userDetailsService;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService());
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.cors() // Uses the CorsConfig bean
				.and()
				.authorizeHttpRequests(auth -> auth
						// Static resource access
						.requestMatchers(new AntPathRequestMatcher("/**/*.jpeg")).permitAll()
						.requestMatchers(new AntPathRequestMatcher("/**/*.jpg")).permitAll()
						.requestMatchers(new AntPathRequestMatcher("/**/*.png")).permitAll()
						.requestMatchers(new AntPathRequestMatcher("/**/*.gif")).permitAll()
						.requestMatchers(new AntPathRequestMatcher("/**/*.svg")).permitAll()

						// Admin endpoints
						.requestMatchers(
								"/admin/register",
								"/admin/find/**",
								"/admin/login",
								"/admin/update",
								"/admin/forgot-password/request",
								"/admin/forgot-password/verify")
						.permitAll()

						// Employee endpoints
						.requestMatchers(
								"/employee/register",
								"/employee/login")
						.permitAll()

						// SubAdmin endpoints
						.requestMatchers(
								"/api/subadmin/create",
								"/api/subadmin/login",
								"/api/subadmin/update-password/**",
								"/api/subadmin/send-email/**",
								"/api/subadmin/all",
								"/api/subadmin/employees/**",
								"/api/subadmin/subadminbygamil/**",
								"/api/subadmin/delete/**",
								"/api/subadmin/forgot-password/request",
								"/api/subadmin/forgot-password/verify",
								"/api/subadmin/update-fields/**",
								"/api/subadmin/by-company/**",
								"/api/subadmin/status/**",
								"/api/subadmin/update/**")
						.permitAll()

						// SubAdmin employee & attendance routes
						.requestMatchers("/api/subadmin/employee/**").permitAll()

						// All other requests
						.anyRequest().authenticated())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
