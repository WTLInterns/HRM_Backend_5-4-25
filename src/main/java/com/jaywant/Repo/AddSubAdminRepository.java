package com.jaywant.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaywant.Model.AddSubAdmin;
import java.util.List;

@Repository
public interface AddSubAdminRepository extends JpaRepository<AddSubAdmin, Integer> {
    // Additional custom queries (if needed) can be added here.

    Optional<AddSubAdmin> findByEmail(String email);

    List<AddSubAdmin> findByRegistercompanyname(String registercompanyname);

}
