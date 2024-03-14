package com.jhnr.myclub.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(
    name = "app_role",
    uniqueConstraints = {
            @UniqueConstraint(name = "APP_ROLE_UK", columnNames = "rolename")
    }
)
public class AppRole {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "rolename", nullable = false)
    private String roleName;

}
