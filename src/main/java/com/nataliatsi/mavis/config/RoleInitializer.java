package com.nataliatsi.mavis.config;

import com.nataliatsi.mavis.entities.Role;
import com.nataliatsi.mavis.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            for (Role.Values value : Role.Values.values()) {
                if (!roleRepository.existsByName(value.getName())) {
                    Role role = new Role();
                    role.setName(value.getName());
                    roleRepository.save(role);
                }
            }
        };
    }
}
