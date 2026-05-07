package com.gyms.memberships_manager.mapper;

import com.gyms.memberships_manager.dto.MembershipPlanRequest;
import com.gyms.memberships_manager.dto.MembershipPlanResponse;
import com.gyms.memberships_manager.model.MembershipPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MembershipPlanMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gym", ignore = true)
    @Mapping(target = "members", ignore = true)
    MembershipPlan toEntity(MembershipPlanRequest request);

    MembershipPlanResponse toResponse(MembershipPlan plan);
}

