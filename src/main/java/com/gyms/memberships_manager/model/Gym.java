package com.gyms.memberships_manager.model;

import jakarta.persistence.*;
import java.util.List;
@Entity
public class Gym {
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<MembershipPlan> getMembershipPlans() {
        return membershipPlans;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMembershipPlans(List<MembershipPlan> membershipPlans) {
        this.membershipPlans = membershipPlans;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String address;
    private String phoneNumber;

    @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembershipPlan> membershipPlans;
}
