package com.gestao.homologacao.dto;

import com.gestao.homologacao.enums.EmployeeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter @Setter
public class EmployeeApprovalRequestDTO {

    @NotNull(message = "O status deve ser informado")
    private EmployeeStatus status;

    private String observation;

}


