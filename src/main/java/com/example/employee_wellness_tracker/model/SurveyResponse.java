package com.example.employee_wellness_tracker.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

/**
* Entity representing an employee's response to a wellness survey.
* Each response is linked to a specific survey and employee.
*/
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "survey_responses")
public class SurveyResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private SurveyTemplate survey;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;  

    @ElementCollection
    @CollectionTable(name = "survey_answers", joinColumns = @JoinColumn(name = "response_id"))
    @Column(name = "answer")
    private List<Integer> answers; 

    //Calculated well-being score based on survey answers 
    private Double wellBeingScore;  
    
    // Timestamp of when the response was submitted 
    private LocalDateTime submissionDate;
}
