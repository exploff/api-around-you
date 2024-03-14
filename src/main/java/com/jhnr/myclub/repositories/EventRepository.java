package com.jhnr.myclub.repositories;

import com.jhnr.myclub.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}

