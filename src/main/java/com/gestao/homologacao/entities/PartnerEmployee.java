package com.gestao.homologacao.entities;

import com.gestao.homologacao.enums.EmployeeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "partner_employees")
@Getter @Setter
public class PartnerEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String cpf;

    private String position;

    // Iniciando o status padrão do Employee.
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status = EmployeeStatus.PENDING_DOCUMENTS;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Document> documents;




}
