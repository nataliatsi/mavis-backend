package com.nataliatsi.mavis.user.profile.repository;


import com.nataliatsi.mavis.user.profile.entities.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
}
