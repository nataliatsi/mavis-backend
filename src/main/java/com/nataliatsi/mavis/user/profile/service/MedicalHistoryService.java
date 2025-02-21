package com.nataliatsi.mavis.user.profile.service;

import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.repository.UserRepository;
import com.nataliatsi.mavis.user.profile.dto.CreateMedicalHistoryDto;
import com.nataliatsi.mavis.user.profile.dto.GetMedicalHistoryDto;
import com.nataliatsi.mavis.user.profile.entities.MedicalHistory;
import com.nataliatsi.mavis.user.profile.entities.Profile;
import com.nataliatsi.mavis.user.profile.mappers.MedicalHistoryMapper;
import com.nataliatsi.mavis.user.profile.repository.MedicalHistoryRepository;
import com.nataliatsi.mavis.user.profile.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MedicalHistoryService {

    private final ProfileRepository userProfileRepository;
    private final MedicalHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final MedicalHistoryMapper historyMapper;

    public MedicalHistoryService(ProfileRepository userProfileRepository, MedicalHistoryRepository historyRepository, UserRepository userRepository, MedicalHistoryMapper historyMapper) {
        this.userProfileRepository = userProfileRepository;
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
        this.historyMapper = historyMapper;
    }

    public List<GetMedicalHistoryDto> getAllMedicalHistory(Authentication authentication) {
        var userId = findUser(authentication).getUserProfile().getId();

        List<MedicalHistory> medicalHistory = userProfileRepository.findMedicalHistoryByUserId(userId);
        return medicalHistory.stream().map(historyMapper::toReturnDTO).collect(Collectors.toList());
    }


    @Transactional
    public MedicalHistory create(CreateMedicalHistoryDto medicalHistoryVersionDTO, Authentication authentication) {

//        String username = authentication.getName();
//        var user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Profile userProfile =  findUser(authentication).getUserProfile();

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

    private User findUser(Authentication authentication){
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));
    }

}
