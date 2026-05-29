package com.example.bugtracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    @Column(unique = true, nullable = false)
    private String email;
    private String passwordHash;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    private LocalDateTime createdAt;
}