package com.nataliatsi.mavis.controller;

import com.nataliatsi.mavis.dto.UserCreateDTO;
import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v2/users")
@Tag(name = "User", description = "Endpoints for user management")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user in the system based on the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User successfully created",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - invalid data in payload or user already exists",
                            content = @Content
                    )}
    )
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateDTO dto) {
        User user = userService.create(dto);
        URI location = URI.create("/api/v2/users/" + user.getUserId());

        return ResponseEntity.created(location).build();
    }

}
