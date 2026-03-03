package com.nexuspay.auth.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Users")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String password;

    private String transactionPin;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected User(){}

    public User(String name, String cpf, String email, int age, String password, String transactionPin, LocalDateTime timestamp){
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.age = age;
        this.password = password;
        this.transactionPin = transactionPin;
        this.createdAt = timestamp;
    }

    public void setVerified(){
        this.status = UserStatus.VERIFIED;
    }
}
