package com.nataliatsi.mavis.user.profile.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "medical_history_versions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private Integer version;

    @ElementCollection
    private List<String> medications;

    @ElementCollection
    private List<String> allergies;

    @ElementCollection
    @Column(name = "pre_existing_conditions")
    private List<String> preExistingConditions;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
