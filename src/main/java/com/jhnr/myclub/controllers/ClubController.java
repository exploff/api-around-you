package com.jhnr.myclub.controllers;

import com.jhnr.myclub.generated.api.ClubApi;
import com.jhnr.myclub.generated.api.dto.ClubModel;
import com.jhnr.myclub.repositories.ClubRepository;
import com.jhnr.myclub.services.ClubService;
import com.jhnr.myclub.utils.EntityToDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClubController implements ClubApi {

    private ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @Override
    public ResponseEntity<List<ClubModel>> getAllClubs() {
        List<ClubModel> clubs = clubService.findClubs().stream()
                .map(EntityToDtoMapper::convertToClubModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clubs);
    }


}
