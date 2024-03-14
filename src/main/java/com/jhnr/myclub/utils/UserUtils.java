package com.jhnr.myclub.utils;

import com.jhnr.myclub.controllers.form.UserUpdateForm;
import com.jhnr.myclub.entities.AppUser;

public class UserUtils {


    public static AppUser appUserRegisterToAppUser(UserUpdateForm userUpdateForm) {
        AppUser user = new AppUser();
        user.setUserName(userUpdateForm.getUserName());
        user.setFirstName(userUpdateForm.getFirstName());
        user.setLastName(userUpdateForm.getLastName());
        user.setEmail(userUpdateForm.getEmail());
        user.setPassword(userUpdateForm.getNewPassword());
        user.setImage(userUpdateForm.getImage());
        user.setPhoneNumber(userUpdateForm.getPhoneNumber());
        return user;
    }
}
