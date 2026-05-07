package com.gyms.memberships_manager.integration;

import com.gyms.memberships_manager.dto.GymRequest;
import com.gyms.memberships_manager.dto.GymResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class DockerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateGymAndFetchList() {
        GymRequest request = new GymRequest("Docker Gym", "Street 1", "123456789");
        ResponseEntity<GymResponse> createResponse = restTemplate.postForEntity("/api/gyms", request, GymResponse.class);
        assertEquals(201, createResponse.getStatusCode().value());

        ResponseEntity<GymResponse[]> listResponse = restTemplate.getForEntity("/api/gyms", GymResponse[].class);
        assertEquals(200, listResponse.getStatusCode().value());
        assertTrue(listResponse.getBody() != null && listResponse.getBody().length >= 1);
    }
}