package com.gyms.memberships_manager.repository;

import com.gyms.memberships_manager.model.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
    boolean existsByName(String name);
}
