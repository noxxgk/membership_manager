package com.gyms.memberships_manager.repository;


import com.gyms.memberships_manager.model.MembershipPlan;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {

    List<MembershipPlan> findByGymId(Long gymId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM MembershipPlan p WHERE p.id = :id")
    Optional<MembershipPlan> findByIdWithLock(@Param("id") Long id);
}
