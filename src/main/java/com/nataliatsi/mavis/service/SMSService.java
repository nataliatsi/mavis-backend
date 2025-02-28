package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.dto.LocationDto;
import com.nataliatsi.mavis.entities.Profile;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.nataliatsi.mavis.utils.MessageFormatter.buildSMSBody;
import static com.nataliatsi.mavis.utils.MessageFormatter.getEmergencyContactPhoneNumber;


@Service
public class SMSService implements MessageService {

    @Value("${TWILIO_ACCOUNT_SID}")
    private String ACCOUNT_SID;

    @Value("${TWILIO_AUTH_TOKEN}")
    private String AUTH_TOKEN;

    @Value("${TWILIO_PHONE_NUMBER}")
    private String FROM_NUMBER;

    private final FindUser findUser;

    public SMSService(FindUser findUser) {
        this.findUser = findUser;
    }

    @Override
    public void sendMessage(LocationDto locationDto, Authentication authentication) {
        var user = findUser.getAuthenticatedUser(authentication);
        var userProfile = user.getUserProfile();

        String[] phoneNumbers = getEmergencyContactPhoneNumber(userProfile);

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        for (String emergencyPhoneNumber : phoneNumbers) {
            sendMessageToPhoneNumber(emergencyPhoneNumber, userProfile, locationDto);
        }
    }

    private void sendMessageToPhoneNumber(String phoneNumber, Profile userProfile, LocationDto locationDto) {
        Message message = Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(FROM_NUMBER),
                buildSMSBody(userProfile, locationDto)
        ).create();

        System.out.println("Message SID: " + message.getSid());
    }


    @Override
    public String getType() {
        return "sms";
    }

}
