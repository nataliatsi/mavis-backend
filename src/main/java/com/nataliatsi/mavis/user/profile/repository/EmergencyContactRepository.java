package com.nataliatsi.mavis.user.profile.repository;

import com.nataliatsi.mavis.user.profile.entities.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
    Optional<EmergencyContact> findByEmail(String email);
}
