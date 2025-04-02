package com.example.employee_wellness_tracker.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_wellness_tracker.model.User;
import com.example.employee_wellness_tracker.repository.UserRepository;
import com.example.employee_wellness_tracker.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller for handling authentication-related operations such as user registration, login,
 * profile updates, and account deactivation.
 */

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {
        @Autowired
        UserService userService;

        @Autowired
        UserRepository userRepository;

         /**
         * Registers a new user in the system.
         *
         * @param user The user details to be registered.
         * @return ResponseEntity with a success message upon successful registration.
         */
        @PostMapping("/register")
        public ResponseEntity<String> register(@Valid @RequestBody User user) {
            System.out.println("User : "+user);
            userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully");
        }

        /**
         * Authenticates a user based on email and password.
         *
         * @param user The user credentials (email & password) for login.
         * @return ResponseEntity containing user details upon successful authentication, or an error message if authentication fails.
         */
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody User user) {
            // Check if email is valid (must be @nucleusteq.com)
            if (!user.getEmail().matches("^[a-zA-Z0-9._%+-]+@nucleusteq\\.com$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid email. Only company emails (@nucleusteq.com) are allowed.");
            }
            System.out.println("User : " + user);

            Optional<User> existingUser = userService.findByEmail(user.getEmail());
            System.out.println("existingUser : " + existingUser);

            if (existingUser.isPresent() && existingUser.get().getPassword().equals(user.getPassword())) {
                if (!existingUser.get().isActive()) {
                    return ResponseEntity.status(403).body("Account is deactivated. Contact admin to reactivate.");
                }
                User loggedInUser = existingUser.get();
                // Constructing JSON response with all user details
                Map<String, Object> response = new HashMap<>();
                response.put("id", loggedInUser.getId());
                response.put("name", loggedInUser.getName());
                response.put("email", loggedInUser.getEmail());
                response.put("phone", loggedInUser.getPhone());
                response.put("department", loggedInUser.getDepartment());
                response.put("role", loggedInUser.getRole());
                System.out.println("Response"+response);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        }

        /**
         * Updates the profile details of a user.
         *
         * @param updatedUser The updated user details.
         * @param id          The ID of the user whose profile needs to be updated.
         * @return ResponseEntity with a success message if updated, or an error message if the user is not found.
         */
        @PutMapping("/update")
        public ResponseEntity<String> updateProfile(@Valid @RequestBody User updatedUser, @RequestParam Long id) {
            try {
                boolean isUpdated = userService.updateUser(id, updatedUser);
                if (isUpdated) {
                    return ResponseEntity.ok("Profile updated successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or unauthorized");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile: " + e.getMessage());
            }
        }
        
         /**
         * Deactivates a user account by setting the active status to false.
         *
         * @param id The ID of the user to be deactivated.
         * @return ResponseEntity with a success message if deactivated, or an error message if the user is not found.
         */
        @PutMapping("/deactivate/{id}")
        public ResponseEntity<String> deactivateAccount(@PathVariable Long id) {
            Optional<User> userOptional = userRepository.findById(id);
            
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setActive(false); // Set user as inactive
                userRepository.save(user);
                return ResponseEntity.ok("Account deactivated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        }


}
