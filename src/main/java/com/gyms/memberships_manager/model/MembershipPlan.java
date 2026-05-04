package com.gyms.memberships_manager.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class MembershipPlan {
        public Long getId() {
            return id;
        }

        public Gym getGym() {
            return gym;
        }

        public String getName() {
            return name;
        }

        public PlanType getType() {
            return type;
        }

        public BigDecimal getMonthlyPriceAmount() {
            return monthlyPriceAmount;
        }

        public String getCurrency() {
            return currency;
        }

        public Integer getDurationMonths() {
            return durationMonths;
        }

        public Integer getMaxMembers() {
            return maxMembers;
        }

        public List<Member> getMembers() {
            return members;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setGym(Gym gym) {
            this.gym = gym;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMonthlyPriceAmount(BigDecimal monthlyPriceAmount) {
            this.monthlyPriceAmount = monthlyPriceAmount;
        }

        public void setType(PlanType type) {
            this.type = type;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public void setDurationMonths(Integer durationMonths) {
            this.durationMonths = durationMonths;
        }

        public void setMaxMembers(Integer maxMembers) {
            this.maxMembers = maxMembers;
        }

        public void setMembers(List<Member> members) {
            this.members = members;
        }

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "gym_id", nullable = false)
        private Gym gym;

        @Column(nullable = false)
        private String name;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private PlanType type;

        @Column(nullable = false)
        private BigDecimal monthlyPriceAmount;

        @Column(nullable = false)
        private String currency;

        @Column(nullable = false)
        private Integer durationMonths;

        @Column(nullable = false)
        private Integer maxMembers;

        @OneToMany(mappedBy = "membershipPlan")
        private List<Member> members;
}
