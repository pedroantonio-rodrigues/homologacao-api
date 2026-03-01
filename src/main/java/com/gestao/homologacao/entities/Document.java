package com.gestao.homologacao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gestao.homologacao.enums.DocumentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "documents")
@Getter @Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String filePath;

    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private PartnerEmployee employee;

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;

    private String observation;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    @OrderBy("createdAt DESC")
    private List<DocumentHistory> history;

    private LocalDateTime uploadDate;

    @PrePersist
    protected void onCreate() {
        this.uploadDate = LocalDateTime.now();
    }

}
