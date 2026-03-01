package com.gestao.homologacao.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter @Setter
public class DocumentResponseDTO {

    private Long id;
    private String fileName;
    private String status;
    private String typeName;
    private LocalDateTime uploadDate;
}
