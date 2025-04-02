package com.example.employee_wellness_tracker.service;

import com.example.employee_wellness_tracker.model.SurveyResponse;
import com.example.employee_wellness_tracker.repository.SurveyResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.*;

/**
* Service class for generating wellness reports and exporting them as CSV.
*/
@Service
public class ReportService {

    @Autowired
    private SurveyResponseRepository surveyResponseRepository;
    
    /**
    * Retrieves the wellness report of employees who have submitted responses.
    *
    * @return List of maps containing employee wellness report details.
    */
    public List<Map<String, Object>> getWellnessReport() {
        List<Object[]> results = surveyResponseRepository.findEmployeeWellnessReport();
        List<Map<String, Object>> reportList = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> report = new HashMap<>();
            report.put("employeeId", result[0]);
            report.put("name", result[1]);
            report.put("title", result[2]);
            report.put("score", result[3]);
            reportList.add(report);
        }
        return reportList;
    }

    /**
     * Exports the wellness report of all employees to a CSV file.
     *
     * @param writer The PrintWriter object to write the CSV content.
     */
    public void exportToCSV(PrintWriter writer) {
        List<SurveyResponse> responses = surveyResponseRepository.findAll();
        writer.println("Employee ID,Name,Wellness Score,Submission Date");

        for (SurveyResponse response : responses) {
            writer.println(response.getEmployee().getId() + ","
                    + response.getEmployee().getName() + ","
                    + (response.getWellBeingScore() != null ? response.getWellBeingScore() : "N/A") + ","
                    + (response.getSubmissionDate() != null ? response.getSubmissionDate() : "N/A"));
        }
    }

    /**
     * Exports the wellness report of a specific employee to a CSV file.
     *
     * @param writer     The PrintWriter object to write the CSV content.
     * @param employeeId The ID of the employee whose wellness report needs to be exported.
     */
    public void exportToCSV(PrintWriter writer, Long employeeId) {
        List<SurveyResponse> responses;

        if (employeeId != null) {
            responses = surveyResponseRepository.findByEmployeeId(employeeId); // Fetch specific employee's reports
        } else {
            responses = surveyResponseRepository.findAll(); // Fetch all reports
        }

        writer.println("Employee ID,Name,Wellness Score,Submission Date");

        if (responses.isEmpty()) {
            writer.println("No wellness report available for Employee ID: " + (employeeId != null ? employeeId : "All"));
        } else {
            for (SurveyResponse response : responses) {
                writer.println(response.getEmployee().getId() + ","
                        + response.getEmployee().getName() + ","
                        + (response.getWellBeingScore() != null ? response.getWellBeingScore() : "N/A") + ","
                        + (response.getSubmissionDate() != null ? response.getSubmissionDate() : "N/A"));
            }
        }

        writer.flush();
    }
}
