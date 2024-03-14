package com.jhnr.myclub.services;

import com.jhnr.myclub.exceptions.AccountException;
import com.jhnr.myclub.entities.AppRole;
import com.jhnr.myclub.entities.AppUser;

import java.util.List;

public interface AccountService {

    AppUser addUser(AppUser appUser);

    boolean existUserByUserName(String userName);

    AppUser getUser(String userName);

    AppUser updateUser(AppUser oldUser, AppUser newUser) throws AccountException;

    AppUser updateUserImage(AppUser appUser, String image);

    AppRole getRole(String roleName);

    AppRole addRole(AppRole appRole);

    void addRoleToUser(String userName, String roleName);

    AppUser deleteUser(AppUser user);

    List<AppUser> findUsers();

    void updatePassword(AppUser user, String oldPassword, String newPassword) throws AccountException;

}
