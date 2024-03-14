package com.jhnr.myclub.repositories;

import com.jhnr.myclub.entities.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
}

