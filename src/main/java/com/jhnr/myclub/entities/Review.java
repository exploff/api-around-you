package com.jhnr.myclub.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCommentaire;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "id_club")
    private Club club;

    private int note;

    private String commentaire;
}
