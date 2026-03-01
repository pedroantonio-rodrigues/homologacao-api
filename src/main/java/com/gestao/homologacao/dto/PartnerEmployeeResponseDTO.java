package com.gestao.homologacao.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PartnerEmployeeResponseDTO {

    private Long id;
    private String name;
    private String cpf;
    private String status;
    private String partnerName;
    private List<DocumentResponseDTO> documents;
    private List<PartnerEmployeeHistoryResponseDTO> histories;

}
