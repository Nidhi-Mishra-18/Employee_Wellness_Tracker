package com.example.employee_wellness_tracker.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_wellness_tracker.model.SurveyResponse;
import com.example.employee_wellness_tracker.service.SurveyResponseService;

/**
* Controller for handling survey response-related API endpoints.
* Provides functionalities for submitting, retrieving, and deleting survey responses.
*/
@RestController
@RequestMapping("/api/responses")
public class SurveyResponseController {

    @Autowired
    private SurveyResponseService surveyResponseService;
    /**
    * Submits a new survey response.
    *
    * @param surveyResponse The survey response submitted by an employee.
    * @return ResponseEntity containing the saved SurveyResponse object.
    */
    @PostMapping("/submit")
    public ResponseEntity<SurveyResponse> submitResponse(@RequestBody SurveyResponse surveyResponse) {
        return ResponseEntity.ok(surveyResponseService.submitResponse(surveyResponse));
    }

    /**
    * Retrieves the survey response history for a specific employee.
    *
    * @param employeeId ID of the employee whose survey responses are requested.
    * @return ResponseEntity containing a list of SurveyResponse objects.
    */
    @GetMapping("/history/{employeeId}")
    public ResponseEntity<List<SurveyResponse>> getEmployeeResponses(@PathVariable Long employeeId) {
        return ResponseEntity.ok(surveyResponseService.getResponsesByEmployee(employeeId));
    }

    /**
    * Deletes a survey response by its ID.
    *
    * @param responseId ID of the survey response to be deleted.
    * @return ResponseEntity with a success message or an error message if deletion is forbidden.
    */
    @DeleteMapping("/delete/{responseId}")
    public ResponseEntity<?> deleteResponse(@PathVariable Long responseId) {
        try {
            surveyResponseService.deleteResponse(responseId);
            return ResponseEntity.ok(Collections.singletonMap("message", "Survey response deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Updates an existing survey response.
     *
     * @param id The ID of the survey response to update.
     * @param updatedResponse The updated SurveyResponse object.
     * @return ResponseEntity containing the updated SurveyResponse.
     */
    @PutMapping("/update/{responseId}")
    public ResponseEntity<?> updateSurveyResponse(@PathVariable Long responseId, @RequestBody Map<String, List<Integer>> requestBody) {
        List<Integer> updatedAnswers = requestBody.get("answers");

        SurveyResponse updatedResponse = surveyResponseService.updateSurveyResponse(responseId, updatedAnswers);

        return ResponseEntity.ok(updatedResponse);
    }

}

