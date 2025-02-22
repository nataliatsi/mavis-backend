package com.nataliatsi.mavis.user.profile.controller;

import com.nataliatsi.mavis.user.profile.dto.CreateMedicalHistoryDto;
import com.nataliatsi.mavis.user.profile.dto.GetMedicalHistoryDto;
import com.nataliatsi.mavis.user.profile.entities.MedicalHistory;
import com.nataliatsi.mavis.user.profile.service.MedicalHistoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users/profiles/medical-history")
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;


    public MedicalHistoryController(MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<GetMedicalHistoryDto>> getAllMedicalHistory (Authentication authentication){
        var medicalHistory = medicalHistoryService.getAllMedicalHistory(authentication);
        return ResponseEntity.ok(medicalHistory);
    }

    @PostMapping
    public ResponseEntity<MedicalHistory> addMedicalHistory(
            @Valid @RequestBody CreateMedicalHistoryDto dto,
            Authentication authentication){
        var newMedicalHistory = medicalHistoryService.create(dto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMedicalHistory);
    }
}
