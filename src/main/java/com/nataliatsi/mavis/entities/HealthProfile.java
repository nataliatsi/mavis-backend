package com.nataliatsi.mavis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_health_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blood_type")
    private String bloodType;

    private Double height;

    private Double weight;

    @ElementCollection
    @CollectionTable(name = "health_profile_allergies", joinColumns = @JoinColumn(name = "health_profile_id"))
    @Column(name = "allergy")
    private List<String> allergies = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "health_profile_chronic_conditions", joinColumns = @JoinColumn(name = "health_profile_id"))
    @Column(name = "condition")
    private List<String> chronicConditions = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "health_profile_family_genetic_diseases", joinColumns = @JoinColumn(name = "health_profile_id"))
    @Column(name = "disease")
    private List<String> familyGeneticDiseases = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "health_profile_important_vaccines", joinColumns = @JoinColumn(name = "health_profile_id"))
    @Column(name = "vaccine")
    private List<String> importantVaccines = new ArrayList<>();

    private Boolean smoker;

    private Boolean alcoholic;

    @ElementCollection
    @CollectionTable(name = "health_profile_dietary_restrictions", joinColumns = @JoinColumn(name = "health_profile_id"))
    @Column(name = "restriction")
    private List<String> dietaryRestrictions = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}

