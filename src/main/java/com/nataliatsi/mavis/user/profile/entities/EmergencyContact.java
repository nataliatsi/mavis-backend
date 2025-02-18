package com.nataliatsi.mavis.user.profile.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "emergency_contacts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String relationship;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

}

