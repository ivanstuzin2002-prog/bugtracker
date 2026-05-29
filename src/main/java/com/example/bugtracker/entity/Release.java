package com.example.bugtracker.entity;

import com.example.bugtracker.entity.enums.ReleaseStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "releases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Release {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "version_id")
    private Version version;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    private LocalDate releaseDate;
    @Enumerated(EnumType.STRING)
    private ReleaseStatus status;
}