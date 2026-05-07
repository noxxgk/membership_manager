package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.GymRequest;
import com.gyms.memberships_manager.dto.GymResponse;

import java.util.List;

public interface GymService {
    GymResponse createGym(GymRequest request);

    List<GymResponse> getAllGyms();
}
