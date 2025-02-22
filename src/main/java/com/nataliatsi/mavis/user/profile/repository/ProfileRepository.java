package com.nataliatsi.mavis.user.profile.repository;

import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.user.profile.entities.EmergencyContact;
import com.nataliatsi.mavis.user.profile.entities.MedicalHistory;
import com.nataliatsi.mavis.user.profile.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT u.emergencyContacts FROM Profile u WHERE u.id = :userId")
    List<EmergencyContact> findEmergencyContactsByUserId(@Param("userId") Long userId);

    @Query("SELECT e FROM Profile u JOIN u.emergencyContacts e WHERE u.id = :userId AND e.id = :contactId")
    Optional<EmergencyContact> findEmergencyContactById(@Param("userId") Long userId, @Param("contactId") Long contactId);

    @Query("SELECT u.medicalHistory FROM Profile u WHERE u.id = :userId")
    List<MedicalHistory> findMedicalHistoryByUserId(@Param("userId") Long userId);

}
