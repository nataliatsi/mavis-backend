package com.nataliatsi.mavis.controller;

import com.nataliatsi.mavis.dto.UserRequestDTO;
import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDTO dto) {
        User user = userService.create(dto);
        URI location = URI.create("/api/users/" + user.getUserId());

        return ResponseEntity.created(location).build();
    }

}
