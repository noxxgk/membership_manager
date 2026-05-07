package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.MembershipPlanRequest;
import com.gyms.memberships_manager.dto.MembershipPlanResponse;
import com.gyms.memberships_manager.exception.ResourceNotFoundException;
import com.gyms.memberships_manager.mapper.MembershipPlanMapper;
import com.gyms.memberships_manager.model.Gym;
import com.gyms.memberships_manager.model.MembershipPlan;
import com.gyms.memberships_manager.repository.GymRepository;
import com.gyms.memberships_manager.repository.MembershipPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MembershipPlanServiceImpl implements MembershipPlanService {

    private final MembershipPlanRepository planRepository;
    private final GymRepository gymRepository;
    private final MembershipPlanMapper membershipPlanMapper;

    @Override
    public MembershipPlanResponse createPlan(Long gymId, MembershipPlanRequest request) {
        log.info("Attempting to create plan for gym ID: {}", gymId);
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> {
                    log.warn("Gym with ID {} not found.", gymId);
                    return new ResourceNotFoundException("Gym with ID " + gymId + " not found.");
                });
        MembershipPlan plan = membershipPlanMapper.toEntity(request);
        plan.setGym(gym);
        MembershipPlan savedPlan = planRepository.save(plan);
        log.info("Successfully created membership plan with ID: {}", savedPlan.getId());
        return membershipPlanMapper.toResponse(savedPlan);
    }

    @Override
    public List<MembershipPlanResponse> getPlansByGymId(Long gymId) {
        log.info("Fetching plans for gym ID: {}", gymId);
        if (!gymRepository.existsById(gymId)) {
            log.warn("Gym with ID {} not found.", gymId);
            throw new ResourceNotFoundException("Gym with ID " + gymId + " not found.");
        }

        return planRepository.findByGymId(gymId)
                .stream()
                .map(membershipPlanMapper::toResponse)
                .toList();
    }
}