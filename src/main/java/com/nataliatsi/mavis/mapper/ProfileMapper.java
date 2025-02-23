package com.nataliatsi.mavis.mapper;

import com.nataliatsi.mavis.dto.*;
import com.nataliatsi.mavis.entities.Address;
import com.nataliatsi.mavis.entities.EmergencyContact;
import com.nataliatsi.mavis.entities.Location;
import com.nataliatsi.mavis.entities.Profile;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProfileMapper {

    GetProfileDto toDTO(Profile userProfile);
    Profile toUserProfile(CreateProfileDto userRegister);
    Address toAddress(AddressDto addressDTO);
    EmergencyContact toEmergencyContact(EmergencyContactDto emergencyContactDTO);
    EmergencyContactDto toEmergencyContactDTO(EmergencyContact emergencyContact);
    GetEmergencyContactDto toGetEmergencyContactDTO(EmergencyContact emergencyContact);
    Location toLocation(LocationDto locationDTO);
    LocationDto toLocationDTO(Location location);
}
