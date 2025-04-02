package com.example.employee_wellness_tracker.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employee_wellness_tracker.model.Role;
import com.example.employee_wellness_tracker.model.User;
import com.example.employee_wellness_tracker.service.UserService;

/**
 * Controller for handling admin-related operations such as managing employees.
 * Provides CRUD functionalities for employee management.
 */

@CrossOrigin
@RestController 
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * Creates a new employee and saves it in the database.
     *
     * @param user Employee details to be created.
     * @return ResponseEntity containing the created employee object.
     */
    @PostMapping("/employees")
    public ResponseEntity<User> createEmployee(@RequestBody User user) {
        return ResponseEntity.ok(userService.createEmployee(user));
    }

    
    /**
     * Retrieves a list of all employees.
     *
     * @return ResponseEntity containing a list of all employees or 204 No Content if empty.
     */
    @GetMapping("/employees")
    public ResponseEntity<List<User>> getAllEmployees() {
        List<User> employees = userService.getAllEmployees();
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 if no employees found
        }
        return ResponseEntity.ok(employees);
    }

     /**
     * Retrieves a specific employee by their ID.
     *
     * @param id Employee ID to be fetched.
     * @return ResponseEntity containing the employee details or 404 Not Found if employee does not exist.
     */
    @GetMapping("/employees/{id}")
    public ResponseEntity<User> getEmployeeById(@PathVariable Long id) {
        return userService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

     /**
     * Updates an existing employee's details.
     *
     * @param id          Employee ID to be updated.
     * @param updatedUser Updated employee details.
     * @return ResponseEntity containing success message if updated, or 404 Not Found if employee does not exist.
     */
    @PutMapping("/employees/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateEmployee(id, updatedUser)
                ? ResponseEntity.ok("Employee updated successfully")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
    }

     /**
     * Deletes an employee by their ID.
     *
     * @param id Employee ID to be deleted.
     * @return ResponseEntity containing success message if deleted, or 404 Not Found if employee does not exist.
     */
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        return userService.deleteEmployee(id)
                ? ResponseEntity.ok("Employee deleted successfully")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
    }
    

    /**
     * Updates an employee's role.
     *
     * @param id      Employee ID whose role needs to be updated.
     * @param newRole New role to be assigned to the employee.
     * @return ResponseEntity containing the updated employee details.
     */
    @PutMapping("/employees/{id}/role")
    public ResponseEntity<User> updateRole(@PathVariable Long id, @RequestParam Role newRole) {
       User updatedEmployee = userService.updateEmployeeRole(id, newRole);
        return ResponseEntity.ok(updatedEmployee);
    }
}

