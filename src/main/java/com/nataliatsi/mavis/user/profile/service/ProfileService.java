package com.nataliatsi.mavis.user.profile.service;

import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.repository.UserRepository;
import com.nataliatsi.mavis.user.profile.dto.CreateProfileDto;
import com.nataliatsi.mavis.user.profile.entities.EmergencyContact;
import com.nataliatsi.mavis.user.profile.entities.Profile;
import com.nataliatsi.mavis.user.profile.mappers.ProfileMapper;
import com.nataliatsi.mavis.user.profile.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.profileMapper = profileMapper;
    }

    @Transactional
    public Profile createUserProfile(CreateProfileDto dto, Authentication authentication){
        if(dto == null){
            throw new IllegalArgumentException("Dados do usuário são obrigatórios");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        Profile newProfile = profileMapper.toUserProfile(dto);
        newProfile.setUser(user);

        List<EmergencyContact> emergencyContacts = dto.emergencyContacts().stream()
                .map(profileMapper::toEmergencyContact)
                .toList();
        newProfile.setEmergencyContacts(emergencyContacts);

        Profile savedProfile = profileRepository.save(newProfile);

        user.setUserProfile(savedProfile);
        userRepository.save(user);

        return newProfile;
    }

}
