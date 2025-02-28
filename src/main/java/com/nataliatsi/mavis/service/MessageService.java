package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.dto.LocationDto;
import org.springframework.security.core.Authentication;

public interface MessageService {
    void sendMessage(LocationDto locationDto, Authentication authentication);
    String getType();
}
