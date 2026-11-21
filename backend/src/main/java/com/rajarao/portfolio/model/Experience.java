package com.rajarao.portfolio.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Experience {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String company;
    private String role;
    private String location;
    private String period;
    private boolean current;
    private String domain;
    /** Comma separated, e.g. "Java,Spring Boot,React" */
    @Column(length = 500)
    private String tech;
    private int sortOrder;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    @Column(length = 800)
    private List<String> highlights;
}
