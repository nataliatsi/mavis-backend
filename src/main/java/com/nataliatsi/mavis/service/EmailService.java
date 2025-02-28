package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.dto.LocationDto;
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


}
