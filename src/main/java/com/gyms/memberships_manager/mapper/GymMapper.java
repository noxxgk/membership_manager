package com.gyms.memberships_manager.mapper;

import com.gyms.memberships_manager.dto.GymRequest;
import com.gyms.memberships_manager.dto.GymResponse;
import com.gyms.memberships_manager.model.Gym;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GymMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "membershipPlans", ignore = true)
    Gym toEntity(GymRequest request);

    GymResponse toResponse(Gym gym);
}

