package com.nataliatsi.mavis.controller;

import com.nataliatsi.mavis.dto.user.PasswordSuccessResponseDTO;
import com.nataliatsi.mavis.dto.user.PasswordUpdateRequestDTO;
import com.nataliatsi.mavis.dto.user.UserCreateRequestDTO;
import com.nataliatsi.mavis.dto.user.UserCreateResponseDTO;
import com.nataliatsi.mavis.service.FindUser;
import com.nataliatsi.mavis.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v2/users")
@Tag(name = "User", description = "Endpoints for user management")
public class UserController {
    private final UserService userService;
    private final FindUser findUser;

    public UserController(UserService userService, FindUser findUser) {
        this.userService = userService;
        this.findUser = findUser;
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
    public ResponseEntity<UserCreateResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO dto) {
        UserCreateResponseDTO response = userService.create(dto);
        URI location = URI.create("/api/v2/users/" + response.userId());

        return ResponseEntity.created(location).body(response);
    }

    @Operation(
            summary = "Change user password",
            description = "Allows a logged-in user to change their password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PasswordUpdateRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Password updated successfully",
                            content = @Content(
                                    schema = @Schema(implementation = PasswordSuccessResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - invalid input"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - JWT missing or invalid"
                    )}
    )
    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody PasswordUpdateRequestDTO request, Authentication authentication) {
        userService.updatePassword(request.oldPassword(), request.newPassword(), authentication);
        return ResponseEntity.ok(new PasswordSuccessResponseDTO("Password updated successfully"));
    }
}
