package com.example.employee_wellness_tracker.model;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

/**
* Entity representing a wellness survey template.
* This template contains a list of questions that employees will respond to.
*/
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "survey_templates")
public class SurveyTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ElementCollection
    @CollectionTable(name = "survey_questions", joinColumns = @JoinColumn(name = "survey_id"))
    @Column(name = "question")
    private List<String> questions;
}
