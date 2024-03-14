package com.jhnr.myclub.entities;

import com.jhnr.myclub.entities.enums.ClubTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private Double longitude;
    private Double latitude;
    private String adresse;
    private String telephone;
    private String image;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ClubTypes> types = new ArrayList<>();


    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "club")
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany(mappedBy = "clubs")
    private List<AppUser> users = new ArrayList<>();
}
