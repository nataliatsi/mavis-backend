package com.nataliatsi.mavis.controller;

import com.jayway.jsonpath.JsonPath;
import com.nataliatsi.mavis.entities.Address;
import com.nataliatsi.mavis.entities.Role;
import com.nataliatsi.mavis.entities.User;
import com.nataliatsi.mavis.repository.RoleRepository;
import com.nataliatsi.mavis.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final String URL_LOGIN = "/api/login";

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = new Role();
        role.setName(Role.Values.ADMIN.getName());
        role = roleRepository.save(role);

        Address address = new Address();
        address.setStreet("Rua das Flores");
        address.setNumber("123B");
        address.setNeighborhood("Centro");
        address.setCity("São Paulo");
        address.setState("SP");
        address.setPostalCode("01000-000");
        address.setReferencePoint("Próximo à padaria Pão de Mel");

        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin@test.com");
        user.setPhoneNumber("11999999999");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setRoles(Set.of(role));
        user.setFullName("Admin Test");
        user.setBirthDate(LocalDate.of(1998, 3, 15));
        user.setAddress(address);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar token JWT com credenciais válidas")
    void deveRetornarTokenComCredenciaisValidas() throws Exception {

        var response = mockMvc.perform(post(URL_LOGIN)
                        .with(httpBasic("admin", "Test@1234")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.token").exists())
                .andReturn();

        String json = response.getResponse().getContentAsString();
        String token = JsonPath.read(json, "$.token");

        mockMvc.perform(get("/api/v1/protected/hello").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 401 para usuário inexistente")
    void deveRetornar401ParaUsuarioInexistente() throws Exception {
        mockMvc.perform(post(URL_LOGIN)
                        .with(httpBasic("usuarioInvalido", "qualquerSenha")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 401 para senha incorreta")
    void deveRetornar401ParaSenhaIncorreta() throws Exception {
        mockMvc.perform(post(URL_LOGIN)
                        .with(httpBasic("admin", "senhaErrada")))
                .andExpect(status().isUnauthorized());
    }

}
