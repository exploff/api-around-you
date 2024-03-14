package com.jhnr.myclub.controllers.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserRegisterForm {

    private String userName;

    private String email;

    private String password;
}
