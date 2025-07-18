package com.nataliatsi.mavis.controller;

import com.nataliatsi.mavis.dto.UserRequestDTO;
import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Endpoints for user management")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user in the system based on the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User successfully created",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request - invalid data in payload",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Business rule violation - email, phone number or username already in use",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDTO dto) {
        User user = userService.create(dto);
        URI location = URI.create("/api/users/" + user.getUserId());

        return ResponseEntity.created(location).build();
    }

}
