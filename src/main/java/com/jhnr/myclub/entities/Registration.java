package com.jhnr.myclub.entities;

import com.jhnr.myclub.entities.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "id_club")
    private Club club;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;
}
