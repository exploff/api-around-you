package com.jhnr.myclub.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClubTypes {
    SPORT("sport"),
    RESTAURANT("restaurant"),
    CINEMA("cinema"),
    THEATRE("theatre"),
    MONUMENT("monument"),
    CULTURE("culture");


    private final String type;
}
