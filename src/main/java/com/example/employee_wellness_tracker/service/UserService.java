package com.example.employee_wellness_tracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employee_wellness_tracker.model.Role;
import com.example.employee_wellness_tracker.model.User;
import com.example.employee_wellness_tracker.repository.UserRepository;

/**
 * Service class for handling user-related operations such as 
 * registration, role management, and CRUD operations for employees.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Registers a new user in the system.
     * @param user User object containing registration details.
     * @return The saved User object.
     */
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Finds a user by their email.
     * @param email The email to search for.
     * @return An Optional containing the user if found.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Updates an existing user's details.
     * @param id The ID of the user to update.
     * @param user The updated user details.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateUser(Long id, User user) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setName(user.getName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPhone(user.getPhone());
            updatedUser.setDepartment(user.getDepartment());
            updatedUser.setPassword(user.getPassword()); // Ideally, password should be hashed

            userRepository.save(updatedUser);
            return true;
        }
        return false;
    }

    /**
     * Creates a new employee with a default role of EMPLOYEE.
     * @param user User object with employee details.
     * @return The created employee.
     */
    public User createEmployee(User user) {
        user.setRole(Role.EMPLOYEE);
        return userRepository.save(user);
    }

    /**
     * Retrieves a list of all employees.
     * @return List of users with role EMPLOYEE.
     */
    public List<User> getAllEmployees() {
        return userRepository.findByRole(Role.EMPLOYEE);
    }

    /**
     * Retrieves an employee by their ID.
     * @param id The employee ID.
     * @return An Optional containing the employee if found.
     */
    public Optional<User> getEmployeeById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Updates an employee's details.
     * @param id The ID of the employee.
     * @param updatedUser The updated employee details.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateEmployee(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPhone(updatedUser.getPhone());
            user.setDepartment(updatedUser.getDepartment());
            user.setActive(updatedUser.isActive()); // Handles activation/deactivation of user
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    /**
     * Deletes an employee by their ID.
     * @param id The employee ID.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteEmployee(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Upgrades employee's role (e.g., from EMPLOYEE to ADMIN).
     * @param employeeId The ID of the employee.
     * @param newRole The new role to assign.
     * @return The updated User object with the new role.
     */
    public User updateEmployeeRole(Long employeeId, Role newRole) {
        User user = userRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        user.setRole(newRole);
        return userRepository.save(user);
    }
}
