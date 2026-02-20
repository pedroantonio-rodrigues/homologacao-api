package com.gestao.homologacao.entities;

import com.gestao.homologacao.enums.PartnerStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "partners")
@Getter @Setter
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String cnpj;

    @Enumerated(EnumType.STRING)
    private PartnerStatus partnerStatus;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL)
    private List<PartnerEmployee> employees;
}