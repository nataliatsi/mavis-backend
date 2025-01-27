package com.nataliatsi.mavis.controller;

import com.nataliatsi.mavis.dto.UserRegisterDto;
import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.repository.RoleRepository;
import com.nataliatsi.mavis.repository.UserRepository;
import com.nataliatsi.mavis.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        try {
            User user = userService.registerUser(userRegisterDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error registering user: " + e.getMessage());
        }
    }
}
