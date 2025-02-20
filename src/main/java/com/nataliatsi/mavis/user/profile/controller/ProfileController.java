package com.nataliatsi.mavis.user.profile.controller;

import com.nataliatsi.mavis.user.profile.dto.AddressDto;
import com.nataliatsi.mavis.user.profile.dto.CreateProfileDto;
import com.nataliatsi.mavis.user.profile.dto.GetProfileDto;
import com.nataliatsi.mavis.user.profile.dto.UpdateProfileDto;
import com.nataliatsi.mavis.user.profile.entities.Profile;
import com.nataliatsi.mavis.user.profile.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/profiles")
public class ProfileController {

    private final ProfileService userProfileService;

    public ProfileController(ProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public ResponseEntity<Profile> registerUserProfile(
            @Valid @RequestBody CreateProfileDto dataUserRegister,
            Authentication authentication) {

        Profile createdUser = userProfileService.createUserProfile(dataUserRegister, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        try {
            GetProfileDto userProfileDTO = userProfileService.getUserProfile(authentication);
            if (userProfileDTO != null) {
                return ResponseEntity.ok(userProfileDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Perfil não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno no servidor: " + e.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateUserProfile(
            @Valid @RequestBody UpdateProfileDto userDataUpdate,
            Authentication authentication ) {
        try {
            userProfileService.updateUserProfile(userDataUpdate, authentication);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno no servidor: " + e.getMessage());
        }
    }

    @PutMapping("/address")
    public ResponseEntity<?> updateAddress(
            @RequestBody @Valid AddressDto addressDTO,
            Authentication authentication) {
        try {
            Profile updatedUserProfile = userProfileService.updateAddress( addressDTO, authentication);
            return ResponseEntity.ok(updatedUserProfile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erro interno no servidor: " + e.getMessage());
        }
    }

}
