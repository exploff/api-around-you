package com.jhnr.myclub.repositories;

import com.jhnr.myclub.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUserName(String userName);

    boolean existsAppUserByUserName(String userName);

}
