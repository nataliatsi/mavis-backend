package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.dto.UserRegisterDto;
import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private UserRegisterDto userRegisterDto;

    @BeforeEach
    void setUp() {
        userRegisterDto = new UserRegisterDto(
                "testuser",
                "password123",
                "testuser@example.com"
        );
    }

    @Test
    @Transactional
    void shouldSaveANewUserInTheDB() {
        User user = userService.registerUser(userRegisterDto);

        assertNotNull(user);
        assertEquals(userRegisterDto.username(), user.getUsername());
        assertEquals(userRegisterDto.email(), user.getEmail());
        assertTrue(passwordEncoder.matches(userRegisterDto.password(), user.getPassword()));

        User persistedUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        assertNotNull(persistedUser);
        assertEquals(user.getUsername(), persistedUser.getUsername());
    }
}
