package com.nataliatsi.mavis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tb_appointment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Column(name = "professional_name")
    private String professionalName;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "location")
    private String location;

    @Column(name = "appointment_date")
    private LocalDate date;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    private MedicalHistory medicalHistory;
}

