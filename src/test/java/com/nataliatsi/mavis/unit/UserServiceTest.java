package com.nataliatsi.mavis.unit;

import com.nataliatsi.mavis.dto.RoleDTO;
import com.nataliatsi.mavis.dto.user.UserCreateRequestDTO;
import com.nataliatsi.mavis.dto.user.UserCreateResponseDTO;
import com.nataliatsi.mavis.entities.Role;
import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.exception.UserAlreadyExistsException;
import com.nataliatsi.mavis.mapper.UserMapper;
import com.nataliatsi.mavis.repository.RoleRepository;
import com.nataliatsi.mavis.repository.UserRepository;
import com.nataliatsi.mavis.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
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
    public void create_ShouldReturnUser_WhenDtoIsValid() {

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
    public void create_ShouldThrowException_WhenEmailAlreadyExists() {

        when(passwordEncoder.encode(requestDTO.password())).thenReturn("encodedPass");
        when(roleRepository.findByName("BASIC")).thenReturn(java.util.Optional.of(basicRole));
        when(userMapper.toEntity(requestDTO)).thenReturn(userEntity);

        when(userRepository.saveAndFlush(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry"));


        assertThrows(UserAlreadyExistsException.class, () -> userService.create(requestDTO));
        verify(userRepository, times(1)).saveAndFlush(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when BASIC role not found")
    public void create_ShouldThrowException_WhenRoleDoesNotFound() {

        when(roleRepository.findByName("BASIC")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.create(requestDTO));

        verify(userRepository, times(0)).saveAndFlush(any(User.class));
    }
}
