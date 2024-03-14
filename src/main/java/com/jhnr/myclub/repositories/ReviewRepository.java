package com.jhnr.myclub.repositories;

import com.jhnr.myclub.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
