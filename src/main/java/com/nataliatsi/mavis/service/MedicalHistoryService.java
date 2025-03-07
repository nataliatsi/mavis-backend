package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.repository.UserRepository;
import com.nataliatsi.mavis.dto.CreateMedicalHistoryDto;
import com.nataliatsi.mavis.dto.GetMedicalHistoryDto;
import com.nataliatsi.mavis.entities.MedicalHistory;
import com.nataliatsi.mavis.entities.Profile;
import com.nataliatsi.mavis.mapper.MedicalHistoryMapper;
import com.nataliatsi.mavis.repository.MedicalHistoryRepository;
import com.nataliatsi.mavis.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MedicalHistoryService {

    private final ProfileRepository userProfileRepository;
    private final MedicalHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final MedicalHistoryMapper historyMapper;
    private final FindUser findUser;

    public MedicalHistoryService(ProfileRepository userProfileRepository, MedicalHistoryRepository historyRepository, UserRepository userRepository, MedicalHistoryMapper historyMapper, FindUser findUser) {
        this.userProfileRepository = userProfileRepository;
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
        this.historyMapper = historyMapper;
        this.findUser = findUser;
    }

    public List<GetMedicalHistoryDto> getAllMedicalHistory(Authentication authentication) {
        var userId = findUser.getAuthenticatedUser(authentication).getUserProfile().getId();

        List<MedicalHistory> medicalHistory = userProfileRepository.findMedicalHistoryByUserId(userId);
        return medicalHistory.stream().map(historyMapper::toReturnDTO).collect(Collectors.toList());
    }

    @Transactional
    public MedicalHistory create(CreateMedicalHistoryDto medicalHistoryVersionDTO, Authentication authentication) {

        Profile userProfile =  findUser.getAuthenticatedUser(authentication).getUserProfile();

        var teste = userProfile.getId();
        System.out.println(teste);

        if (userProfile.getMedicalHistory().size() >= 5) {
            userProfile.getMedicalHistory().remove(0);
        }

        int newVersion = userProfile.getMedicalHistory().stream()
                .mapToInt(MedicalHistory::getVersion)
                .max()
                .orElse(0) + 1;

        MedicalHistory newMedicalHistory = historyMapper.toMedicalHistoryWithCreatedAt(medicalHistoryVersionDTO);
        newMedicalHistory.setVersion(newVersion);
        newMedicalHistory = historyRepository.save(newMedicalHistory);

        userProfile.getMedicalHistory().add(newMedicalHistory);
        userProfileRepository.save(userProfile);

        return newMedicalHistory;
    }

}
