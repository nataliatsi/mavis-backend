package com.nataliatsi.mavis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_medical_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "medicalHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "performed_procedures", joinColumns = @JoinColumn(name = "medical_history_id"))
    @Column(name = "procedure")
    private List<String> performedProcedures = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "important_exams", joinColumns = @JoinColumn(name = "medical_history_id"))
    private List<ImportantExam> importantExams = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "recent_symptoms", joinColumns = @JoinColumn(name = "medical_history_id"))
    @Column(name = "symptom")
    private List<String> recentSymptoms = new ArrayList<>();

    @Column(name = "last_appointment_date")
    private LocalDate lastAppointmentDate;

    @Column(name = "next_appointment_date")
    private LocalDate nextAppointmentDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "health_profile_id")
    private HealthProfile healthProfile;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

