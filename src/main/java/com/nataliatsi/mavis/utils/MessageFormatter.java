package com.nataliatsi.mavis.utils;

import com.nataliatsi.mavis.dto.LocationDto;
import com.nataliatsi.mavis.entities.EmergencyContact;
import com.nataliatsi.mavis.entities.MedicalHistory;
import com.nataliatsi.mavis.entities.Profile;

public class MessageFormatter {

    public static String getFormattedDateOfBirth(Profile userProfile) {
        return userProfile.getDateOfBirth() != null ? userProfile.getDateOfBirth().toString() : "Não informado";
    }

    public static String getFormattedAddress(Profile userProfile) {
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

    public static String getFormattedLocationText(LocationDto currentLocation) {
        if (currentLocation == null) {
            return "Não informado";
        }

        return String.format(
                "Latitude: %.5f, Longitude: %.5f\n" +
                        "Veja a localização no Google Maps: https://www.google.com/maps?q=%f,%f",
                currentLocation.latitude(),
                currentLocation.longitude(),
                currentLocation.latitude(),
                currentLocation.longitude()
        );
    }

    public static String[] getEmergencyContactEmails(Profile userProfile) {
        return userProfile.getEmergencyContacts().stream()
                .map(EmergencyContact::getEmail)
                .filter(email -> email != null && !email.isBlank())
                .toArray(String[]::new);
    }

    public static String[] getEmergencyContactPhoneNumber(Profile userProfile) {
        return userProfile.getEmergencyContacts().stream()
                .map(EmergencyContact::getPhoneNumber)
                .filter(email -> email != null && !email.isBlank())
                .toArray(String[]::new);
    }

    public static String getFormattedMedicalHistory(Profile userProfile) {
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

    public static String buildEmailBody(Profile userProfile, LocationDto currentLocation) {
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

    public static String buildSMSBody(Profile userProfile, LocationDto currentLocation) {
        String fullName = userProfile.getFullName();
        String latitude = String.valueOf(currentLocation.latitude());
        String longitude = String.valueOf(currentLocation.longitude());

        String locationLink = String.format("https://www.google.com/maps?q=%s,%s", latitude, longitude);

        return String.format(
                "Olá, %s acionou um pedido de ajuda. Veja a localização no Maps: %s. Tente contato imediato.",
                fullName, locationLink
        );
    }


}
