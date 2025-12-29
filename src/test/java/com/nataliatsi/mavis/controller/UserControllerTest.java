package com.nataliatsi.mavis.controller;

import com.nataliatsi.mavis.dto.user.UserCreateRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
public class UserControllerTest {

//    @Autowired
//    private TestRestTemplate restTemplate = new TestRestTemplate();
//
//    @Test
//    void shouldReturn201WhenUserIsCreated() {
//        UserCreateRequestDTO dto = new UserCreateRequestDTO(
//                "usertest",
//                "test@example.com",
//                "+5511900002222",
//                "Password@123"
//        );
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<UserCreateRequestDTO> request = new HttpEntity<>(dto, headers);
//        ResponseEntity<Void> response = restTemplate.postForEntity("/api/v2/users", request, Void.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(response.getHeaders().getLocation()).isNotNull();
//        assertThat(response.getHeaders().getLocation().toString()).startsWith("/api/v2/users/");
//    }
}