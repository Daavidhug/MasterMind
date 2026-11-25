package com.mastermind.MasterMind.model;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique =true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;







}
