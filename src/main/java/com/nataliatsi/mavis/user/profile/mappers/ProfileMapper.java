package com.nataliatsi.mavis.user.profile.mappers;

import com.nataliatsi.mavis.user.profile.dto.*;
import com.nataliatsi.mavis.user.profile.entities.Address;
import com.nataliatsi.mavis.user.profile.entities.EmergencyContact;
import com.nataliatsi.mavis.user.profile.entities.Location;
import com.nataliatsi.mavis.user.profile.entities.Profile;
import com.nataliatsi.mavis.user.profile.repository.ProfileRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    GetProfileDto toDTO(ProfileRepository userProfile);

    Profile toUserProfile(CreateProfileDto userRegister);
    Address toAddress(AddressDto addressDTO);
    EmergencyContact toEmergencyContact(EmergencyContactDto emergencyContactDTO);
    EmergencyContactDto toEmergencyContactDTO(EmergencyContact emergencyContact);
    Location toLocation(LocationDto locationDTO);
}
