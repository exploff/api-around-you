package com.jhnr.myclub.services.impl;

import com.jhnr.myclub.exceptions.AccountException;
import com.jhnr.myclub.entities.AppRole;
import com.jhnr.myclub.entities.AppUser;
import com.jhnr.myclub.repositories.AppRoleRepository;
import com.jhnr.myclub.repositories.AppUserRepository;
import com.jhnr.myclub.services.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    // Méthode transactionnelle, lorsqu'on termine l'execution d'une méthode il fait un commit dans la DB

    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;

    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addUser(AppUser appUser) {

        String password = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(password));

        appUser.setProfileCompleted(false);

        return appUserRepository.save(appUser);
    }

    @Override
    public boolean existUserByUserName(String userName) {
        return appUserRepository.existsAppUserByUserName(userName);
    }

    @Override
    public AppUser updateUser(AppUser oldUser, AppUser newUser) throws AccountException {

        // Si l'username a changé et qu'il existe déjà, déclenche exception
        if (newUser.getUserName() != null &&
            !oldUser.getUserName().equalsIgnoreCase(newUser.getUserName()))
        {
            if (this.existUserByUserName(newUser.getUserName())) {
                throw new AccountException("Username déjà existant");
            } else {
                oldUser.setUserName(newUser.getUserName());
            }
        }

        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }

        if (newUser.getImage() != null) {
            oldUser.setImage(newUser.getImage());
        }

        if (newUser.getFirstName() != null) {
            oldUser.setFirstName(newUser.getFirstName());
        }

        if (newUser.getLastName() != null) {
            oldUser.setLastName(newUser.getLastName());
        }

        if (newUser.getPhoneNumber() != null) {
            oldUser.setPhoneNumber(newUser.getPhoneNumber());
        }

        return oldUser;
    }

    @Override
    public AppUser updateUserImage(AppUser appUser, String image) {

        appUser.setImage(image);

        return appUser;
    }

    @Override
    public AppUser getUser(String userName) {
        return appUserRepository.findByUserName(userName);
    }

    @Override
    public AppRole getRole(String roleName) {
        return appRoleRepository.findByRoleName(roleName);
    }

    @Override
    public AppRole addRole(AppRole appRole) {
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
        AppUser appUser = appUserRepository.findByUserName(userName);

        AppRole appRole = appRoleRepository.findByRoleName(roleName);

        appUser.getAppRoles().add(appRole);
    }

    @Override
    public AppUser deleteUser(AppUser user) {
        appUserRepository.delete(user);
        return user;
    }

    @Override
    public List<AppUser> findUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public void updatePassword(AppUser user, String oldPassword, String newPassword) throws AccountException {

        if (passwordEncoder.matches(user.getPassword(), oldPassword)) {
            throw new AccountException("Old password doesn't match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
    }
}
