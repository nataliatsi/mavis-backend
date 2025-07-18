package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.dto.UserRequestDTO;
import com.nataliatsi.mavis.entities.Role;
import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.exception.UserAlreadyExistsException;
import com.nataliatsi.mavis.mapper.UserMapper;
import com.nataliatsi.mavis.repository.RoleRepository;
import com.nataliatsi.mavis.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public User create(UserRequestDTO dto) {
        String encryptedPassword = passwordEncoder.encode(dto.password());
        Role basicRole = roleRepository.findByName(Role.Values.BASIC.name()).orElseThrow();

        if (userRepository.findByUsername(dto.username()).isPresent()) throw new UserAlreadyExistsException("User already exists.");

        User user = userMapper.toEntity(dto);
        user.setPassword(encryptedPassword);
        user.setRoles(Set.of(basicRole));

        user = userRepository.save(user);
        return user;
    }
}
