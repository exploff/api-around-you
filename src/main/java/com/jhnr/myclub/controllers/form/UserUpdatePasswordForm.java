package com.jhnr.myclub.controllers.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePasswordForm {

    private String oldPassword;
    private String newPassword;
}
