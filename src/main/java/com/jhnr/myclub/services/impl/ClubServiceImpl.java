package com.jhnr.myclub.services.impl;

import com.jhnr.myclub.entities.Club;
import com.jhnr.myclub.repositories.ClubRepository;
import com.jhnr.myclub.services.ClubService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClubServiceImpl implements ClubService {

    private ClubRepository clubRepository;
    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public List<Club> findClubs() {
        return clubRepository.findAll();
    }
}
