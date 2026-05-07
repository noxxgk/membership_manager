package com.gyms.memberships_manager.mapper;

import com.gyms.memberships_manager.dto.MemberRegistrationRequest;
import com.gyms.memberships_manager.dto.MemberResponse;
import com.gyms.memberships_manager.model.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "membershipPlan", ignore = true)
    Member toEntity(MemberRegistrationRequest request);

    @Mapping(source = "membershipPlan.name", target = "planName")
    @Mapping(source = "membershipPlan.gym.name", target = "gymName")
    MemberResponse toResponse(Member member);
}

