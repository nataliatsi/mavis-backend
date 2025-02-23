package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.dto.LocationDto;
import com.nataliatsi.mavis.entities.EmergencyContact;
import com.nataliatsi.mavis.entities.MedicalHistory;
import com.nataliatsi.mavis.entities.Profile;
import com.nataliatsi.mavis.mapper.ProfileMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final FindUser findUser;
    private final ProfileMapper profileMapper;

    public EmailService(JavaMailSender mailSender, FindUser findUser, ProfileMapper profileMapper) {
        this.mailSender = mailSender;
        this.findUser = findUser;
        this.profileMapper = profileMapper;
    }

    public void sendEmail(LocationDto location, Authentication authentication) {
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
            e.printStackTrace();
        }
    }

    private String[] getEmergencyContactEmails(Profile userProfile) {
        return userProfile.getEmergencyContacts().stream()
                .map(EmergencyContact::getEmail)
                .toArray(String[]::new);
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

    private String getFormattedDateOfBirth(Profile userProfile) {
        return userProfile.getDateOfBirth() != null ? userProfile.getDateOfBirth().toString() : "Não informado";
    }

    private String getFormattedAddress(Profile userProfile) {
        return userProfile.getAddress() != null
                ? String.format("%s, %s - %s, %s/%s, CEP: %s",
                userProfile.getAddress().getStreet(),
                userProfile.getAddress().getNumber(),
                userProfile.getAddress().getNeighborhood(),
                userProfile.getAddress().getCity(),
                userProfile.getAddress().getState(),
                userProfile.getAddress().getPostalCode())
                : "Não informado";
    }

    private String getFormattedLocationText(LocationDto currentLocation) {
        return String.format(
                "Latitude: %.5f, Longitude: %.5f\n" +
                        "Veja a localização no Google Maps: https://www.google.com/maps?q=%f,%f",
                currentLocation.latitude(),
                currentLocation.longitude(),
                currentLocation.latitude(),
                currentLocation.longitude()
        );
    }

    private String getFormattedMedicalHistory(Profile userProfile) {
        if (userProfile.getMedicalHistory() == null || userProfile.getMedicalHistory().isEmpty()) {
            return "Nenhuma informação médica disponível.";
        }

        MedicalHistory latestHistory = userProfile.getMedicalHistory().get(0);

        String medications = latestHistory.getMedications() != null && !latestHistory.getMedications().isEmpty()
                ? String.join(", ", latestHistory.getMedications())
                : "Nenhuma medicação registrada.";

        String allergies = latestHistory.getAllergies() != null && !latestHistory.getAllergies().isEmpty()
                ? String.join(", ", latestHistory.getAllergies())
                : "Nenhuma alergia registrada.";

        String preExistingConditions = latestHistory.getPreExistingConditions() != null && !latestHistory.getPreExistingConditions().isEmpty()
                ? String.join(", ", latestHistory.getPreExistingConditions())
                : "Nenhuma condição pré-existente registrada.";

        return String.format(
                """
                - Medicações em uso: %s
                - Alergias: %s
                - Condições pré-existentes: %s
                """,
                medications, allergies, preExistingConditions
        );
    }


}
