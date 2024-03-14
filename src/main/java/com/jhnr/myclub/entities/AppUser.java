package com.jhnr.myclub.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(
    name = "app_user",
    uniqueConstraints = { //
            @UniqueConstraint(name = "APP_USER_UK", columnNames = "username")
    }
)
public class AppUser {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phoneNumber", unique = true)
    private String phoneNumber;

    @Column(name = "image")
    private String image;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @UpdateTimestamp
    private LocalDateTime createdDate;

    @CreationTimestamp
    private LocalDateTime modifiedDate;

    @Column(name = "isProfileCompleted")
    private boolean isProfileCompleted;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AppRole> appRoles = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_events",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_event"))
    @JsonIgnore
    private List<Event> events = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_club_registration",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_club")
    )
    @JsonIgnore
    private List<Club> clubs = new ArrayList<>();
}
