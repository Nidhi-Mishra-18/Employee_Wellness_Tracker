package com.example.employee_wellness_tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employee_wellness_tracker.model.Role;
import com.example.employee_wellness_tracker.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role); // Fetch employees only
}
