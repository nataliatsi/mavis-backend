package com.nataliatsi.mavis.mapper;

import com.nataliatsi.mavis.dto.CreateMedicalHistoryDto;
import com.nataliatsi.mavis.dto.GetMedicalHistoryDto;
import com.nataliatsi.mavis.entities.MedicalHistory;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface MedicalHistoryMapper {

    MedicalHistory toEntity(CreateMedicalHistoryDto dto);

    GetMedicalHistoryDto toReturnDTO(MedicalHistory dto);

    default MedicalHistory toMedicalHistoryWithCreatedAt(CreateMedicalHistoryDto dto) {
        MedicalHistory medicalHistory = toEntity(dto);
        medicalHistory.setCreatedAt(LocalDateTime.now());
        return medicalHistory;
    }
}
