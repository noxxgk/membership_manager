package com.gyms.memberships_manager.service;

import com.gyms.memberships_manager.dto.MemberRegistrationRequest;
import com.gyms.memberships_manager.model.Gym;
import com.gyms.memberships_manager.model.MembershipPlan;
import com.gyms.memberships_manager.model.PlanType;
import com.gyms.memberships_manager.repository.GymRepository;
import com.gyms.memberships_manager.repository.MembershipPlanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MembershipConcurrencyTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private MembershipPlanRepository planRepository;

    @Test
    void shouldPreventOverbookingWithConcurrentRequests() throws InterruptedException {
        Gym gym = new Gym();
        gym.setName("Sync Gym");
        gym.setAddress("Street 1");
        gym.setPhoneNumber("123456789");
        gymRepository.save(gym);

        MembershipPlan plan = new MembershipPlan();
        plan.setName("Exclusive Plan");
        plan.setType(PlanType.PREMIUM);
        plan.setCurrency("EUR");
        plan.setMonthlyPriceAmount(new BigDecimal("100.00"));
        plan.setDurationMonths(12);
        plan.setMaxMembers(2);
        plan.setGym(gym);
        plan = planRepository.save(plan);

        int numberOfThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        Long planId = plan.getId();
        for (int i = 0; i < numberOfThreads; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    MemberRegistrationRequest req = new MemberRegistrationRequest(
                            "Runner " + finalI, "run" + finalI + "@test.com", planId);
                    memberService.registerMember(req);
                    successCount.incrementAndGet();
                } catch (IllegalArgumentException e) {
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        assertEquals(2, successCount.get(), "Only 2 members should be successfully registered.");
        assertEquals(3, failureCount.get(), "3 members should be rejected due to capacity limit.");
    }
}