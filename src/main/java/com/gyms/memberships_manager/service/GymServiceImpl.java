package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.GymRequest;
import com.gyms.memberships_manager.dto.GymResponse;
import com.gyms.memberships_manager.model.Gym;
import com.gyms.memberships_manager.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GymServiceImpl implements GymService{
    private final GymRepository gymRepository;

    @Override
    public GymResponse createGym(GymRequest request) {
        log.info("Attempting to create gym with name: {}", request.name());
        if (gymRepository.existsByName(request.name())) {
            log.warn("Gym with name '{}' already exists.", request.name());
            throw new IllegalArgumentException("Gym with name '" + request.name() + "' already exists.");
        }
        Gym gym = new Gym();
        gym.setName(request.name());
        gym.setAddress(request.address());
        gym.setPhoneNumber(request.phoneNumber());
        Gym savedGym = gymRepository.save(gym);
        log.info("Successfully created gym with ID: {}", savedGym.getId());
        return mapToResponse(savedGym);
    }

    @Override
    public List<GymResponse> getAllGyms() {
        log.info("Fetching all gyms");
        return gymRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private GymResponse mapToResponse(Gym gym) {
        return new GymResponse(
                gym.getId(),
                gym.getName(),
                gym.getAddress(),
                gym.getPhoneNumber()
        );
    }
}
