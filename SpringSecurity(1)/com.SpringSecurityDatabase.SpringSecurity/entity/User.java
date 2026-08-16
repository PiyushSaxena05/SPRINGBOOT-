package com.SpringSecurityDatabase.SpringSecurity.entity;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

       @Column(nullable = false,unique = true)
    String username;

    @Column(nullable = false)
    private String password;

    private Boolean enabled = true;

 
    
}
