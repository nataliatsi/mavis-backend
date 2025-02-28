package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.dto.LocationDto;
import com.nataliatsi.mavis.entities.Profile;
import com.nataliatsi.mavis.mapper.ProfileMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.nataliatsi.mavis.utils.MessageFormatter.*;

@Service
public class EmailService implements MessageService {

    private final JavaMailSender mailSender;
    private final FindUser findUser;
    private final ProfileMapper profileMapper;

    public EmailService(JavaMailSender mailSender, FindUser findUser, ProfileMapper profileMapper) {
        this.mailSender = mailSender;
        this.findUser = findUser;
        this.profileMapper = profileMapper;
    }

    public void sendMessage(LocationDto location, Authentication authentication) {
        try {
            var user = findUser.getAuthenticatedUser(authentication);
            var userProfile = user.getUserProfile();

            String[] emails = getEmergencyContactEmails(userProfile);

            userProfile.setLocation(profileMapper.toLocation(location));
            var currentLocation = profileMapper.toLocationDTO(userProfile.getLocation());

            var message = new SimpleMailMessage();
            message.setFrom(user.getEmail());
            message.setTo(emails);
            message.setSubject("🚨 Notificação de Emergência - " + userProfile.getFullName());
            message.setText(buildEmailBody(userProfile, currentLocation));

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    @Override
    public String getType() {
        return "email";
    }


    private String buildEmailBody(Profile userProfile, LocationDto currentLocation) {
        String fullName = userProfile.getFullName();
        String dateOfBirth = getFormattedDateOfBirth(userProfile);
        String address = getFormattedAddress(userProfile);
        String locationText = getFormattedLocationText(currentLocation);
        String medicalHistoryText = getFormattedMedicalHistory(userProfile);

        return String.format(
                """
                Olá,
        
                %s acionou um pedido de ajuda no aplicativo MAVIS. Você está recebendo esta mensagem porque foi cadastrado como contato de emergência dele(a).
        
                Abaixo estão as informações mais recentes de %s:
        
                - 📅 Data de Nascimento: %s
                - 🏠 Endereço: %s
                - 📍 Localização Atual: %s
                
                🏥 Informações Médicas:
                %s
        
                ⚠️ Por favor, tente entrar em contato com %s imediatamente para verificar a situação.
                Se não conseguir contato e houver indícios de perigo, acione os serviços de emergência.
        
                Obrigado por estar disponível para ajudar!
        
                Atenciosamente,
                Equipe MAVIS
                """,
                fullName, fullName, dateOfBirth, address, locationText, medicalHistoryText, fullName
        );
    }

}
