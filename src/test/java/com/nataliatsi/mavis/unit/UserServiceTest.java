package com.nataliatsi.mavis.unit;

import com.nataliatsi.mavis.dto.UserCreateDTO;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

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

    private UserCreateDTO dto;
    private Role basicRole;
    private User userEntity;

    @BeforeEach
    void setUp() {
        dto = new UserCreateDTO(
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

        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPass");
        when(roleRepository.findByName("BASIC")).thenReturn(java.util.Optional.of(basicRole));
        when(userMapper.toEntity(dto)).thenReturn(userEntity);
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        User result = userService.create(dto);

        assertEquals("usertest", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("+5511900002222", result.getPhoneNumber());
        assertEquals(Set.of(basicRole), result.getRoles());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    public void create_ShouldThrowException_WhenEmailAlreadyExists() {

        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPass");
        when(roleRepository.findByName("BASIC")).thenReturn(java.util.Optional.of(basicRole));
        when(userMapper.toEntity(dto)).thenReturn(userEntity);

        when(userRepository.save(any(User.class))).thenThrow(new org.springframework.dao.DataIntegrityViolationException("Duplicate entry"));

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(dto));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when BASIC role not found")
    public void create_ShouldThrowException_WhenRoleDoesNotFound() {

        when(roleRepository.findByName("BASIC")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.create(dto));

        verify(userRepository, times(0)).save(any(User.class));
    }
}
