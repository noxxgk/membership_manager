package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.GymRequest;
import com.gyms.memberships_manager.dto.GymResponse;
import com.gyms.memberships_manager.model.Gym;
import com.gyms.memberships_manager.repository.GymRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GymServiceImpl implements GymService{
    private final GymRepository gymRepository;
    public GymServiceImpl(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }
    @Override
    public GymResponse createGym(GymRequest request) {
        if (gymRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Gym with name '" + request.name() + "' already exists.");
        }
        Gym gym = new Gym();
        gym.setName(request.name());
        gym.setAddress(request.address());
        gym.setPhoneNumber(request.phoneNumber());
        Gym savedGym = gymRepository.save(gym);
        return mapToResponse(savedGym);
    }

    @Override
    public List<GymResponse> getAllGyms() {
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
