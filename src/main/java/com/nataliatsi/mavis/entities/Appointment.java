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

    private String specialty;

    @Column(name = "appointment_location")
    private String appointmentLocation;

    @Column(name = "appointment_date")
    private LocalDate date;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "health_profile_id")
    private HealthProfile healthProfile;
}

