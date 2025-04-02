package com.example.employee_wellness_tracker.Controller;


import com.example.employee_wellness_tracker.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* Controller for handling wellness report-related API endpoints.
* This provides APIs to generate wellness reports and export them as CSV files.
*/
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
    * Constructor for ReportController.
    * @param reportService Service to handle report generation and export.
    */
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    
    /**
    * Retrieves the wellness report of employees who have submitted responses.
    * @return List of wellness reports containing employee wellness data.
    */
    @GetMapping("/wellness-report")
    public List<Map<String, Object>> getWellnessReport() {
        return reportService.getWellnessReport();
    }
    
    /**
    * Exports the complete wellness report as a CSV file.
    * @param response HTTP response to write the CSV data.
    * @throws IOException If an I/O error occurs while writing the CSV file.
    */
    @GetMapping("/export/csv")
    public void exportCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=wellness_report.csv");
        reportService.exportToCSV(response.getWriter());
    }

    /**
    * Exports an individual employee's wellness report as a CSV file.
    * @param employeeId ID of the employee whose report is to be exported.
    * @param response HTTP response to write the CSV data.
    * @throws IOException If an I/O error occurs while writing the CSV file.
    */
    @GetMapping("/export/csv/{employeeId}")
    public void exportEmployeeCSV(@PathVariable Long employeeId, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=wellness_report_" + employeeId + ".csv");
        reportService.exportToCSV(response.getWriter(), employeeId);
    }
}


