package com.example.employee_wellness_tracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employee_wellness_tracker.model.SurveyTemplate;
import com.example.employee_wellness_tracker.repository.SurveyTemplateRepository;

/**
 * Service class for handling survey templates.
 */
@Service
public class SurveyTemplateService {

    @Autowired
    private SurveyTemplateRepository surveyTemplateRepository;

    /**
     * Creates and saves a new survey template.
     */
    public SurveyTemplate createSurvey(SurveyTemplate surveyTemplate) {
        return surveyTemplateRepository.save(surveyTemplate);
    }

    /**
     * Updates an existing survey template.
     * Throws an exception if the survey is not found.
     */
    public SurveyTemplate updateSurvey(Long id, SurveyTemplate surveyTemplate) {
        SurveyTemplate existingSurvey = surveyTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
        existingSurvey.setTitle(surveyTemplate.getTitle());
        existingSurvey.setQuestions(surveyTemplate.getQuestions());
        return surveyTemplateRepository.save(existingSurvey);
    }

    /**
     * Deletes a survey template by ID.
     */
    public void deleteSurvey(Long id) {
        surveyTemplateRepository.deleteById(id);
    }

    /**
     * Retrieves all available survey templates.
     */
    public List<SurveyTemplate> getAllSurveys() {
        return surveyTemplateRepository.findAll();
    }

    /**
     * Retrieves a survey template by ID.
     * Throws an exception if not found.
     */
    public Optional<SurveyTemplate> getSurveyById(Long id) {
        return surveyTemplateRepository.findById(id);
    }
}
