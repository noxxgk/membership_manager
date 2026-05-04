package com.gyms.memberships_manager.controller;

import com.gyms.memberships_manager.dto.GymRequest;
import com.gyms.memberships_manager.dto.GymResponse;
import com.gyms.memberships_manager.service.GymService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gyms")
public class GymController {

    private final GymService gymService;
    public GymController(GymService gymService) {
        this.gymService = gymService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GymResponse createGym(@Valid @RequestBody GymRequest request) {
        return gymService.createGym(request);
    }
    @GetMapping
    public List<GymResponse> getAllGyms() {
        return gymService.getAllGyms();
    }
}