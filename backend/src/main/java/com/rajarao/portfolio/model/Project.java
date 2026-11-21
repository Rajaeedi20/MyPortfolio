package com.rajarao.portfolio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String company;
    @Column(length = 1000)
    private String description;
    @Column(length = 500)
    private String tech;
    @Column(length = 300)
    private String impact;
    private int sortOrder;
}
