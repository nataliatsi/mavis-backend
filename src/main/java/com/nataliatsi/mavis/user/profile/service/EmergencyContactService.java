package com.nataliatsi.mavis.user.profile.service;

import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.repository.UserRepository;
import com.nataliatsi.mavis.user.profile.dto.EmergencyContactDto;
import com.nataliatsi.mavis.user.profile.dto.GetEmergencyContactDto;
import com.nataliatsi.mavis.user.profile.dto.UpdateEmergencyContactDto;
import com.nataliatsi.mavis.user.profile.entities.EmergencyContact;
import com.nataliatsi.mavis.user.profile.entities.Profile;
import com.nataliatsi.mavis.user.profile.mappers.ProfileMapper;
import com.nataliatsi.mavis.user.profile.repository.EmergencyContactRepository;
import com.nataliatsi.mavis.user.profile.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmergencyContactService {

    private final ProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final EmergencyContactRepository emergencyContactRepository;
    private final ProfileMapper userProfileMapper;
    private final FindUser findUser;

    public EmergencyContactService(ProfileRepository userProfileRepository, UserRepository userRepository, EmergencyContactRepository emergencyContactRepository, ProfileMapper userProfileMapper, FindUser findUser) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.emergencyContactRepository = emergencyContactRepository;
        this.userProfileMapper = userProfileMapper;
        this.findUser = findUser;
    }

    public List<GetEmergencyContactDto> getAllEmergencyContacts(Authentication authentication) {
        var userId = findUser.getAuthenticatedUser(authentication).getUserProfile().getId();
        List<EmergencyContact> contacts = userProfileRepository.findEmergencyContactsByUserId(userId);
        return contacts.stream().map(userProfileMapper::toGetEmergencyContactDTO).collect(Collectors.toList());
    }

    @Transactional
    public EmergencyContact addEmergencyContact(EmergencyContactDto emergencyContactDTO, Authentication authentication) {
        Profile userProfile = findUser.getAuthenticatedUser(authentication).getUserProfile();

        if (userProfile.getEmergencyContacts().size() >= 3) {
            throw new IllegalStateException("Não é possível adicionar mais de 3 contatos de emergência");
        }

        if (emergencyContactDTO.email() != null && !emergencyContactDTO.email().isEmpty()) {
            Optional<EmergencyContact> existingContact = emergencyContactRepository.findByEmail(emergencyContactDTO.email());
            if (existingContact.isPresent()) {
                throw new IllegalArgumentException("Contato de emergência com este email já existe");
            }
        }

        EmergencyContact newContact = userProfileMapper.toEmergencyContact(emergencyContactDTO);
        newContact = emergencyContactRepository.save(newContact);
        userProfile.getEmergencyContacts().add(newContact);
        userProfileRepository.save(userProfile);

        return newContact;
    }

    @Transactional
    public void updateEmergencyContact(Authentication authentication, Long contactId, UpdateEmergencyContactDto updateContact) {

        var userId = findUser.getAuthenticatedUser(authentication).getUserProfile().getId();

        EmergencyContact contact = userProfileRepository.findEmergencyContactById(userId, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado"));

        if(updateContact.name() != null){
            contact.setName(updateContact.name());
        }
        if(updateContact.relationship() != null){
            contact.setRelationship(updateContact.relationship());
        }
        if(updateContact.phoneNumber() != null){
            contact.setPhoneNumber(updateContact.phoneNumber());
        }
        if (updateContact.email() != null) {
            Optional<EmergencyContact> existingContact = emergencyContactRepository.findByEmail(updateContact.email());
            if (existingContact.isPresent() && !existingContact.get().getId().equals(contactId)) {
                throw new IllegalArgumentException("Contato de emergência com este email já existe");
            }
            contact.setEmail(updateContact.email());
        }

        emergencyContactRepository.save(contact);

    }

    @Transactional
    public void deleteEmergencyContact(Authentication authentication, Long contactId) {
        Profile userProfile = findUser.getAuthenticatedUser(authentication).getUserProfile();
        List<EmergencyContact> contactList = userProfile.getEmergencyContacts();

        if (contactList.size() <= 1) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Não foi possível excluir o contato. É necessário ter pelo menos 1 contato de emergência cadastrado.");
        }

        Optional<EmergencyContact> optionalContact = userProfileRepository.findEmergencyContactById(userProfile.getId(), contactId);
        if (optionalContact.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado");
        }

        EmergencyContact contact = optionalContact.get();

        userProfile.getEmergencyContacts().remove(contact);
        userProfileRepository.save(userProfile);

        emergencyContactRepository.delete(contact);

    }
}
