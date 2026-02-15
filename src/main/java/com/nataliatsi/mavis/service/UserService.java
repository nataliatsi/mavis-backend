package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.dto.user.UserCreateRequestDTO;
import com.nataliatsi.mavis.dto.user.UserCreateResponseDTO;
import com.nataliatsi.mavis.entities.Role;
import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.exception.BadRequestException;
import com.nataliatsi.mavis.exception.InvalidPasswordException;
import com.nataliatsi.mavis.exception.RoleNotFoundException;
import com.nataliatsi.mavis.exception.UserAlreadyExistsException;
import com.nataliatsi.mavis.mapper.UserMapper;
import com.nataliatsi.mavis.repository.RoleRepository;
import com.nataliatsi.mavis.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
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
    public UserCreateResponseDTO create(UserCreateRequestDTO dto) {
        Role basicRole = roleRepository.findByName(Role.Values.BASIC.name())
                .orElseThrow(() -> new RoleNotFoundException("Basic role not found"));

        String encryptedPassword = passwordEncoder.encode(dto.password());

        User user = userMapper.toEntity(dto);
        user.setPassword(encryptedPassword);
        user.setRoles(Set.of(basicRole));

        try {
            user = userRepository.saveAndFlush(user);
            return userMapper.toCreateResponse(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserAlreadyExistsException(
                    "A user with this email, phone number or username already exists."
            );
        }
    }

    @Transactional
    public void updatePassword(String oldPassword, String newPassword, Authentication authentication) {
        User user = findUser.getAuthenticatedUser(authentication);
        validatePasswords(oldPassword, newPassword, user);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void validatePasswords(String oldPassword, String newPassword, User user) {
        if (oldPassword == null || oldPassword.isBlank()) {
            throw new InvalidPasswordException("Old password cannot be null or blank");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new InvalidPasswordException("New password cannot be null or blank");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Old password does not match");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new InvalidPasswordException("New password cannot be the same as the old password");
        }
    }
}
