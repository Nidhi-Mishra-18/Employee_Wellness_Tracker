package com.example.employee_wellness_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employee_wellness_tracker.model.SurveyTemplate;

@Repository
public interface SurveyTemplateRepository extends JpaRepository<SurveyTemplate, Long> {
}