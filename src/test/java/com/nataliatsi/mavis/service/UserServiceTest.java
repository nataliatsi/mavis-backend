package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.dto.RoleDTO;
import com.nataliatsi.mavis.dto.user.PasswordUpdateRequestDTO;
import com.nataliatsi.mavis.dto.user.UserCreateRequestDTO;
import com.nataliatsi.mavis.dto.user.UserCreateResponseDTO;
import com.nataliatsi.mavis.entities.Role;
import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.exception.*;
import com.nataliatsi.mavis.mapper.UserMapper;
import com.nataliatsi.mavis.repository.RoleRepository;
import com.nataliatsi.mavis.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private FindUser findUser;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    private UserCreateRequestDTO requestDTO;
    private Role basicRole;
    private final RoleDTO basicRoleDTO = new RoleDTO("BASIC");
    private User userEntity;

    @BeforeEach
    void setUp() {
        requestDTO = new UserCreateRequestDTO(
                "usertest",
                "test@example.com",
                "+5511900002222",
                "Password@123"
        );

        basicRole = new Role();
        basicRole.setName("BASIC");

        userEntity = new User();
        userEntity.setUsername("usertest");
        userEntity.setEmail("test@example.com");
        userEntity.setPhoneNumber("+5511900002222");
        userEntity.setPassword("encodedPass");
        userEntity.setRoles(Set.of(basicRole));
    }


    @Test
    @DisplayName("Should return a saved user when DTO is valid")
    void create_ShouldReturnUser_WhenDtoIsValid() {

        when(passwordEncoder.encode(requestDTO.password())).thenReturn("encodedPass");
        when(roleRepository.findByName("BASIC")).thenReturn(java.util.Optional.of(basicRole));
        when(userMapper.toEntity(requestDTO)).thenReturn(userEntity);
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(userEntity);

        UserCreateResponseDTO responseDTO = new UserCreateResponseDTO(
                UUID.randomUUID(),
                "usertest",
                "test@example.com",
                "+5511900002222",
                Set.of(basicRoleDTO),
                LocalDateTime.now()
        );

        when(userMapper.toCreateResponse(any(User.class))).thenReturn(responseDTO);

        UserCreateResponseDTO result = userService.create(requestDTO);

        assertEquals("usertest", result.username());
        assertEquals("test@example.com", result.email());
        assertEquals("+5511900002222", result.phoneNumber());
        assertEquals(Set.of(basicRoleDTO), result.roles());

        verify(userRepository, times(1)).saveAndFlush(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void create_ShouldThrowException_WhenEmailAlreadyExists() {

        when(passwordEncoder.encode(requestDTO.password())).thenReturn("encodedPass");
        when(roleRepository.findByName("BASIC")).thenReturn(java.util.Optional.of(basicRole));
        when(userMapper.toEntity(requestDTO)).thenReturn(userEntity);

        when(userRepository.saveAndFlush(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry"));


        assertThrows(UserAlreadyExistsException.class, () -> userService.create(requestDTO));
        verify(userRepository, times(1)).saveAndFlush(any(User.class));
    }

    @Test
    @DisplayName("Should throw RoleNotFoundException when BASIC role not found")
    void create_ShouldThrowException_WhenRoleDoesNotFound() {

        when(roleRepository.findByName("BASIC")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userService.create(requestDTO));

        verify(userRepository, times(0)).saveAndFlush(any(User.class));
    }

    @Test
    @DisplayName("Should propagate exception when password encryption fails")
    void create_ShouldThrowException_WhenPasswordEncryptionFails() {

        when(passwordEncoder.encode(requestDTO.password()))
                .thenThrow(new IllegalArgumentException("Encryption failed"));

        when(roleRepository.findByName("BASIC"))
                .thenReturn(Optional.of(basicRole));

        assertThrows(IllegalArgumentException.class,
                () -> userService.create(requestDTO));

        verify(userRepository, never())
                .saveAndFlush(any(User.class));
    }

    @Test
    @DisplayName("Should propagate exception when unexpected persistence error occurs")
    void create_ShouldThrowException_WhenUnexpectedPersistenceErrorOccurs() {

        when(passwordEncoder.encode(requestDTO.password()))
                .thenReturn("encodedPass");

        when(roleRepository.findByName("BASIC"))
                .thenReturn(Optional.of(basicRole));

        when(userMapper.toEntity(requestDTO))
                .thenReturn(userEntity);

        when(userRepository.saveAndFlush(any(User.class)))
                .thenThrow(new RuntimeException("Unexpected DB error"));

        assertThrows(RuntimeException.class,
                () -> userService.create(requestDTO));

        verify(userRepository, times(1))
                .saveAndFlush(any(User.class));
    }

    @Test
    @DisplayName("Should update the password when the user is authenticated, the old password matches, and the new password is valid.")
    void update_ShouldUpdatePassword_WhenUserIsAuthenticatedAndOldPasswordMatchesAndNewPasswordIsValid(){
        String oldPassword = "Password@123";
        String newPassword = "newPassword@123";
        String encodedNewPassword = "encodedNewPassword";

        when(findUser.getAuthenticatedUser(authentication)).thenReturn(userEntity);
        when(passwordEncoder.matches(oldPassword, userEntity.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);

        userService.updatePassword(oldPassword, newPassword, authentication);

        assertEquals(encodedNewPassword, userEntity.getPassword());

        verify(findUser, times(1)).getAuthenticatedUser(authentication);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(userEntity);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Should throw InvalidPasswordException when current password is null or blank")
    void shouldThrowInvalidPasswordException_whenOldPasswordIsNullOrBlank(String oldPassword) {
        String newPassword = "newPassword@123";

        when(findUser.getAuthenticatedUser(authentication)).thenReturn(userEntity);

        assertThrows(InvalidPasswordException.class,
                () -> userService.updatePassword(oldPassword, newPassword, authentication));

        verifyNoInteractions(passwordEncoder);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Should throw InvalidPasswordException when new password is null or blank")
    void shouldThrowInvalidPasswordException_whenNewPasswordIsNullOrBlank(String newPassword) {
        String oldPassword = "Password@123";

        when(findUser.getAuthenticatedUser(authentication)).thenReturn(userEntity);

        assertThrows(InvalidPasswordException.class,
                () -> userService.updatePassword(oldPassword, newPassword, authentication));

        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("Should throw InvalidPasswordException when current password does not match")
    void shouldThrowInvalidPasswordException_whenOldPasswordDoesNotMatch() {
        String oldPassword = "Password@123";
        String newPassword = "newPassword@123";

        when(findUser.getAuthenticatedUser(authentication)).thenReturn(userEntity);
        when(passwordEncoder.matches(oldPassword, userEntity.getPassword())).thenReturn(false);

        assertThrows(InvalidPasswordException.class,
                () -> userService.updatePassword(oldPassword, newPassword, authentication));

    }

    @Test
    @DisplayName("Should throw InvalidPasswordException when new password equals current password")
    void shouldThrowInvalidPasswordException_whenNewPasswordEqualsOldPassword() {
        String oldPassword = "Password@123";
        String newPassword = "Password@123";

        when(findUser.getAuthenticatedUser(authentication)).thenReturn(userEntity);
        when(passwordEncoder.matches(oldPassword, userEntity.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(newPassword, userEntity.getPassword())).thenReturn(true);

        assertThrows(InvalidPasswordException.class,
                () -> userService.updatePassword(oldPassword, newPassword, authentication));
    }
}
