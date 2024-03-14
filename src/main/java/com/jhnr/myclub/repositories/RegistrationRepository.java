package com.jhnr.myclub.repositories;

import com.jhnr.myclub.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}

