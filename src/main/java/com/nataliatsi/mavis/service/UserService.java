package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.dto.UserCreateDTO;
import com.nataliatsi.mavis.entities.Role;
import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.exception.UserAlreadyExistsException;
import com.nataliatsi.mavis.mapper.UserMapper;
import com.nataliatsi.mavis.repository.RoleRepository;
import com.nataliatsi.mavis.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final FindUser findUser;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper, FindUser findUser) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.findUser = findUser;
    }

    @Transactional
    public User create(UserCreateDTO dto) {
        Role basicRole = roleRepository.findByName(Role.Values.BASIC.name())
                .orElseThrow();

        String encryptedPassword = passwordEncoder.encode(dto.password());

        User user = userMapper.toEntity(dto);
        user.setPassword(encryptedPassword);
        user.setRoles(Set.of(basicRole));

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserAlreadyExistsException(
                    "A user with this email, phone number or username already exists."
            );
        }
    }
}
