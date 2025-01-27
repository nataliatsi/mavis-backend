package com.nataliatsi.mavis.controller;

import com.nataliatsi.mavis.security.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(Authentication authentication) {
        String token = authenticationService.authenticate(authentication);
        return ResponseEntity.ok(Map.of("token", token));
    }

}
