package com.nataliatsi.mavis.user.profile.repository;

import com.nataliatsi.mavis.user.profile.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
