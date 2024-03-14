package com.jhnr.myclub.controllers.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserLoginForm {

    private String userName;

    private String password;
}
