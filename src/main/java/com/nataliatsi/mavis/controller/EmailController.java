package com.nataliatsi.mavis.controller;

import com.nataliatsi.mavis.dto.LocationDto;
import com.nataliatsi.mavis.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody LocationDto currentLocation, Authentication authentication) {
        emailService.sendEmail(currentLocation, authentication);
        return ResponseEntity.ok("E-mail enviado com sucesso!");
    }
}
