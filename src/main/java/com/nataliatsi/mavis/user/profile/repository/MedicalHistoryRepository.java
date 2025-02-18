package com.nataliatsi.mavis.user.profile.repository;


import com.nataliatsi.mavis.user.profile.entities.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
}
