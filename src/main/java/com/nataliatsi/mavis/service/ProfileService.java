package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.repository.UserRepository;
import com.nataliatsi.mavis.dto.AddressDto;
import com.nataliatsi.mavis.dto.CreateProfileDto;
import com.nataliatsi.mavis.dto.GetProfileDto;
import com.nataliatsi.mavis.dto.UpdateProfileDto;
import com.nataliatsi.mavis.entities.Address;
import com.nataliatsi.mavis.entities.EmergencyContact;
import com.nataliatsi.mavis.entities.Profile;
import com.nataliatsi.mavis.mapper.ProfileMapper;
import com.nataliatsi.mavis.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private final FindUser findUser;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, ProfileMapper profileMapper, FindUser findUser) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.profileMapper = profileMapper;
        this.findUser = findUser;
    }

    @Transactional
    public Profile createUserProfile(CreateProfileDto dto, Authentication authentication){
        if(dto == null){
            throw new IllegalArgumentException("Dados do usuário são obrigatórios");
        }

        var user = findUser.getAuthenticatedUser(authentication);

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

    public GetProfileDto getUserProfile(Authentication authentication) {
        var user = findUser.getAuthenticatedUser(authentication);
        return profileMapper.toDTO(user.getUserProfile());
    }

    @Transactional
    public void updateUserProfile(UpdateProfileDto dto, Authentication authentication){
        var userProfile = findUser.getAuthenticatedUser(authentication).getUserProfile();

        if(dto.fullName() != null){
            userProfile.setFullName(dto.fullName());

        }
        if(dto.dateOfBirth() != null){
            userProfile.setDateOfBirth(dto.dateOfBirth());
        }
        if(dto.phoneNumber() != null){
            userProfile.setPhoneNumber(dto.phoneNumber());
        }

        profileRepository.save(userProfile);
    }

    @Transactional
    public Profile updateAddress(AddressDto addressDTO, Authentication authentication) {
        Profile userProfile = findUser.getAuthenticatedUser(authentication).getUserProfile();

        Address address = profileMapper.toAddress(addressDTO);
        userProfile.setAddress(address);
        userProfile = profileRepository.save(userProfile);
        return userProfile;
    }

    @Transactional
    public void deleteUserProfile(Authentication authentication) {
        var user = findUser.getAuthenticatedUser(authentication);
        var userProfile = user.getUserProfile();

        if (userProfile != null) {
            profileRepository.delete(userProfile); 
        } else {
            throw new IllegalArgumentException("Perfil não encontrado para o usuário");
        }
    }

}
