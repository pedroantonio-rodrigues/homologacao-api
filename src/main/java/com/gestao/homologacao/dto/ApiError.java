package com.gestao.homologacao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;


@Getter @Setter
@AllArgsConstructor
public class ApiError {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private List<FieldError> fields; // Mostra qual campo falhou


    @Getter @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
    }

}
