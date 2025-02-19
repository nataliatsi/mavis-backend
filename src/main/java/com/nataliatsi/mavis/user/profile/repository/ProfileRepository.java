package com.nataliatsi.mavis.user.profile.repository;

import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.user.profile.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUser(User user);
}
