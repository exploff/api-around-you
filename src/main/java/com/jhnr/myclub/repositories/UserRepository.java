package com.jhnr.myclub.repositories;

import com.jhnr.myclub.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
}
