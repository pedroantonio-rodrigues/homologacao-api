package com.gestao.homologacao.entities;


import com.gestao.homologacao.enums.EmployeeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PartnerEmployeeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus newStatus;

    private LocalDateTime changeDate;
    private String observation;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private PartnerEmployee partnerEmployee;
}
