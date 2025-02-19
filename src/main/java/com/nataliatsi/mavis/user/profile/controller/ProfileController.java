package com.nataliatsi.mavis.user.profile.controller;

import com.nataliatsi.mavis.user.profile.dto.CreateProfileDto;
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

}
