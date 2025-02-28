package com.nataliatsi.mavis.controller;

import com.nataliatsi.mavis.dto.LocationDto;
import com.nataliatsi.mavis.service.MessageSenderStrategy;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final MessageSenderStrategy messageSenderStrategy;

    public NotificationController(MessageSenderStrategy messageSenderStrategy) {
        this.messageSenderStrategy = messageSenderStrategy;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(
            @RequestBody @Valid LocationDto locationDto,
            Authentication authentication) {

        messageSenderStrategy.send(locationDto, authentication);
        return ResponseEntity.ok("Notificações enviadas.");
    }
}
