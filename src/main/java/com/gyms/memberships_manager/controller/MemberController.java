package com.gyms.memberships_manager.controller;

import com.gyms.memberships_manager.dto.MemberRegistrationRequest;
import com.gyms.memberships_manager.dto.MemberResponse;
import com.gyms.memberships_manager.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse registerMember(@Valid @RequestBody MemberRegistrationRequest request) {
        return memberService.registerMember(request);
    }
    @GetMapping
    public List<MemberResponse> getAllMembers() {
        return memberService.getAllMembers();
    }
    @PatchMapping("/{memberId}/cancel")
    public MemberResponse cancelMembership(@PathVariable Long memberId) {
        return memberService.cancelMembership(memberId);
    }
}
