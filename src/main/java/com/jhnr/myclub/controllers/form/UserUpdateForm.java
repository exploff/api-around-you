package com.jhnr.myclub.controllers.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class UserUpdateForm {

    private String email;

    private String userName;

    private String firstName;

    private String lastName;

    private String oldPassword;

    private String newPassword;

    private String image;

    private String phoneNumber;
}
