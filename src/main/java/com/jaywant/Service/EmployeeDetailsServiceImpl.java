package com.jaywant.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jaywant.Model.AddEmployee;
import com.jaywant.Repo.AddEmployeeRepo;

@Service
public class EmployeeDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AddEmployeeRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AddEmployee user = userRepo.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + username);
		}
		return user;
	}
}
