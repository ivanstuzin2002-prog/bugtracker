package com.example.bugtracker.entity;

import com.example.bugtracker.entity.enums.Priority;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bugs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @ManyToOne
    @JoinColumn(name = "version_id")
    private Version version;
    @ManyToOne
    @JoinColumn(name = "reported_by")
    private User reportedBy;
}