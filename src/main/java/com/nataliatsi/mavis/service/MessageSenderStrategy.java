package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.dto.LocationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class MessageSenderStrategy {

    private final Map<String, MessageService> strategies;
    private final FindUser findUser;

    @Autowired
    public MessageSenderStrategy(List<MessageService> services, FindUser findUser) {
        this.strategies = services.stream()
                .collect(Collectors.toMap(MessageService::getType, service -> service));
        this.findUser = findUser;
    }

    public void send(LocationDto locationDto, Authentication authentication) {
        var user = findUser.getAuthenticatedUser(authentication);
        var userProfile = user.getUserProfile();
        var contacts = userProfile.getEmergencyContacts();

        boolean hasEmail = contacts.stream().anyMatch(c -> c.getEmail() != null && !c.getEmail().isBlank());
        boolean hasPhone = contacts.stream().anyMatch(c -> c.getPhoneNumber() != null && !c.getPhoneNumber().isBlank());

        if (!hasEmail && !hasPhone) {
            throw new IllegalStateException("Nenhum contato de emergência possui email ou número de telefone.");
        }

        if (hasEmail) {
            MessageService emailService = strategies.get("email");
            if (emailService != null) {
                emailService.sendMessage(locationDto, authentication);
            }
        }

        if (hasPhone) {
            MessageService smsService = strategies.get("sms");
            if (smsService != null) {
                smsService.sendMessage(locationDto, authentication);
            }
        }
    }
}


