package com.gestao.homologacao.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gestao.homologacao.enums.DocumentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_history")
@Getter @Setter
public class DocumentHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "document_id")
    @JsonIgnore
    private Document document;

    @Enumerated(EnumType.STRING)
    private DocumentStatus statusAtTime;

    @Column(columnDefinition = "TEXT")
    private String observation;

    private LocalDateTime createdAt = LocalDateTime.now();

}
