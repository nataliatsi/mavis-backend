package com.nataliatsi.mavis.user.profile.controller;

import com.nataliatsi.mavis.user.profile.dto.EmergencyContactDto;
import com.nataliatsi.mavis.user.profile.dto.GetEmergencyContactDto;
import com.nataliatsi.mavis.user.profile.dto.UpdateEmergencyContactDto;
import com.nataliatsi.mavis.user.profile.entities.EmergencyContact;
import com.nataliatsi.mavis.user.profile.service.EmergencyContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users/profiles/emergency-contacts")
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;

    public EmergencyContactController(EmergencyContactService emergencyContactService) {
        this.emergencyContactService = emergencyContactService;
    }

    @PostMapping
    public ResponseEntity<EmergencyContact> addEmergencyContact(
            @Valid @RequestBody EmergencyContactDto dto,
            Authentication authentication) {
        var newContact = emergencyContactService.addEmergencyContact(dto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(newContact);
    }

    @GetMapping
    public ResponseEntity<List<GetEmergencyContactDto>> getAllEmergencyContacts(Authentication authentication){
        var contacts = emergencyContactService.getAllEmergencyContacts(authentication);
        return ResponseEntity.ok(contacts);
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<Void> updateContact(
            @PathVariable Long contactId,
            @Valid @RequestBody UpdateEmergencyContactDto dto,
            Authentication authentication){
        emergencyContactService.updateEmergencyContact(authentication, contactId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId, Authentication authentication) {
        try {
            emergencyContactService.deleteEmergencyContact(authentication, contactId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
