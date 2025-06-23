package com.nataliatsi.mavis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    private String frequency;

    private String interval;

    @Column(name = "administration_route")
    private String administrationRoute;

    @Column(name = "start_time")
    private LocalDate startTime;

    @Column(name = "continuous_use")
    private Boolean continuousUse;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "health_profile_id")
    private HealthProfile healthProfile;
}
