package com.jaywant.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jaywant.Model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
  Optional<Admin> findByEmail(String email);
}
