package com.example.employee_wellness_tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.employee_wellness_tracker.model.SurveyResponse;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {

    // Find all responses by a specific employee (matching your service method)
    List<SurveyResponse> findByEmployeeId(Long employeeId);

    // Check if a response exists by ID
    boolean existsById(Long id);

     // Get employees who have submitted responses along with their average well-being score
     @Query("SELECT s.employee.id, s.employee.name, s.survey.title, s.wellBeingScore " +
     "FROM SurveyResponse s JOIN s.survey t")
    List<Object[]> findEmployeeWellnessReport();


}

