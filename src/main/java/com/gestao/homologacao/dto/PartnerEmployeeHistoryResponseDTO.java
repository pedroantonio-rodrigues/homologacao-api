package com.gestao.homologacao.dto;


import com.gestao.homologacao.enums.EmployeeStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PartnerEmployeeHistoryResponseDTO {

    private Long id;
    private EmployeeStatus oldStatus;
    private EmployeeStatus newStatus;
    private LocalDateTime changeDate;
    private String observation;

}
