package com.example.employee_wellness_tracker.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employee_wellness_tracker.model.SurveyResponse;
import com.example.employee_wellness_tracker.repository.SurveyResponseRepository;

/**
 * Service class for handling survey responses.
 */
@Service
public class SurveyResponseService {

    @Autowired
    private SurveyResponseRepository surveyResponseRepository;

    private static final int EDIT_DELETE_TIME_LIMIT = 30; // Time limit (in minutes) for editing/deleting responses

    /**
     * Calculates the well-being score based on survey responses.
     *
     * @param surveyResponse The survey response containing answers.
     * @return The calculated well-being score.
     */
    public double calculateWellBeingScore(SurveyResponse surveyResponse) {
        if (surveyResponse.getAnswers() == null || surveyResponse.getAnswers().isEmpty()) {
            System.out.println("Answers are null or empty, returning default score.");
            return 0.0;  // Default score if no answers are provided
        }

        double sum = surveyResponse.getAnswers().stream().mapToDouble(Integer::doubleValue).sum();
        return sum / surveyResponse.getAnswers().size();  // Example: Average score calculation
    }

    /**
     * Submits a new survey response.
     *
     * @param surveyResponse The response submitted by an employee.
     * @return The saved survey response.
     */
    public SurveyResponse submitResponse(SurveyResponse surveyResponse) {
        surveyResponse.setSubmissionDate(LocalDateTime.now()); // Set submission timestamp
        double wellBeingScore = calculateWellBeingScore(surveyResponse);
        System.out.println("Calculated Well-Being Score: " + wellBeingScore);

        surveyResponse.setWellBeingScore(wellBeingScore);
        return surveyResponseRepository.save(surveyResponse);
    }

    /**
     * Retrieves all responses submitted by a specific employee.
     *
     * @param employeeId The ID of the employee.
     * @return List of survey responses submitted by the employee.
     */
    public List<SurveyResponse> getResponsesByEmployee(Long employeeId) {
        return surveyResponseRepository.findByEmployeeId(employeeId);
    }

    /**
     * Updates an existing survey response, allowed only within 30 minutes of submission.
     *
     * @param responseId      The ID of the response to update.
     * @param updatedResponse The updated response details.
     * @return The updated survey response.
     * @throws RuntimeException if the response is not found or exceeds the 30-minute edit limit.
     */
    @Transactional
    public SurveyResponse updateSurveyResponse(Long responseId, List<Integer> updatedAnswers) {
        SurveyResponse existingResponse = surveyResponseRepository.findById(responseId)
                .orElseThrow(() -> new RuntimeException("Response not found"));

        // Check if the response was submitted within the allowed time frame
        if (Duration.between(existingResponse.getSubmissionDate(), LocalDateTime.now()).toMinutes() > EDIT_DELETE_TIME_LIMIT) {
            throw new RuntimeException("Responses can only be edited within " + EDIT_DELETE_TIME_LIMIT + " minutes of submission.");
        }

        existingResponse.setAnswers(updatedAnswers); // Correctly updating only answers
        return surveyResponseRepository.save(existingResponse);
    }


    /**
     * Deletes a survey response, allowed only within 30 minutes of submission.
     *
     * @param responseId The ID of the response to delete.
     * @throws RuntimeException if the response is not found or exceeds the 30-minute deletion limit.
     */
    @Transactional
    public void deleteResponse(Long responseId) {
        SurveyResponse response = surveyResponseRepository.findById(responseId)
                .orElseThrow(() -> new RuntimeException("Response not found"));

        // Check if the response was submitted within the allowed time frame
        if (Duration.between(response.getSubmissionDate(), LocalDateTime.now()).toMinutes() > EDIT_DELETE_TIME_LIMIT) {
            throw new RuntimeException("Responses can only be deleted within " + EDIT_DELETE_TIME_LIMIT + " minutes of submission.");
        }

        surveyResponseRepository.deleteById(responseId);
    }
}
