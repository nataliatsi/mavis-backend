package com.nataliatsi.mavis.service;

import com.nataliatsi.mavis.dto.UserRegisterDto;
import com.nataliatsi.mavis.entities.Role;
import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.mapper.UserMapper;
import com.nataliatsi.mavis.repository.RoleRepository;
import com.nataliatsi.mavis.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
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
    public User registerUser(UserRegisterDto userRegisterDto) {
        String encryptedPassword = passwordEncoder.encode(userRegisterDto.password());
        Role basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        var userFromDb = userRepository.findByUsername(userRegisterDto.username());
        if(userFromDb.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User user = userMapper.toUser(userRegisterDto);
        user.setPassword(encryptedPassword);

        Set<Role> roles = new HashSet<>();
        roles.add(basicRole);
        user.setRoles(roles);

        user = userRepository.save(user);

        return user;
    }
}
