package com.jhnr.myclub.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_club")
    private Club club;

    private LocalDateTime dateAndTime;
    private String location;
    private String name;

    @ManyToMany(mappedBy = "events")
    private List<AppUser> participants = new ArrayList<>();
}
