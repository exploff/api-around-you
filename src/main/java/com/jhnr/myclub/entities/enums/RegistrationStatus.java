package com.jhnr.myclub.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RegistrationStatus {
    EN_ATTENTE("En Attente"),
    ACCEPTEE("Acceptée"),
    REFUSEE("Refusée"),
    ANNULEE("Annulée");

    private final String status;
}
