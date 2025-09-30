package com.nataliatsi.mavis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tb_medications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String dosage;
    @Column(name = "pharmaceutical_form")
    private String pharmaceuticalForm; // forma farmacêutica (ex.: comprimido, líquido)

    @Column(name = "frequency_per_day")
    private Integer frequencyPerDay;
    @Column(name = "interval_hours")
    private Integer intervalHours;

    @Column(name = "administration_route")
    private String administrationRoute;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "continuous_use")
    private Boolean continuousUse;

    @Column(name = "needs_reminder")
    private Boolean needsReminder;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "health_profile_id")
    private HealthProfile healthProfile;
}