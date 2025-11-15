package com.fileflip.auth_service;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name="tb.users")
public class User{
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID userId;

    private String username;
    private String password;

    // private Set<Role> roles;
}