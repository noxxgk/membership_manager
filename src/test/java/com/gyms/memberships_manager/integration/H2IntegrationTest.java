package com.gyms.memberships_manager.integration;

import com.gyms.memberships_manager.model.Gym;
import com.gyms.memberships_manager.repository.GymRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class H2IntegrationTest {
    @Autowired
    private GymRepository gymRepository;

    @Test
    void shouldSaveAndRetrieveGymFromH2Database() {
        Gym gym = new Gym();
        gym.setName("H2 Test Gym");
        gym.setAddress("Test St 1");
        gym.setPhoneNumber("111-222-333");
        Gym savedGym = gymRepository.save(gym);
        assertNotNull(savedGym.getId(), "The H2 database should assign an ID to the new gym");

        Gym retrievedGym = gymRepository.findById(savedGym.getId()).orElse(null);

        assertNotNull(retrievedGym, "We should find the gym in the H2 database");
        assertEquals("H2 Test Gym", retrievedGym.getName());
        assertEquals("Test St 1", retrievedGym.getAddress());
    }
}