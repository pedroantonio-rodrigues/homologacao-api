package com.gestao.homologacao.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PartnerEmployeeRequestDTO {

    @NotBlank(message = "O nome é obrigatorio")
    private String name;

    @NotBlank(message = "O CPF é obrigatorio")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter extamente 11 numeros")
    private String cpf;



}
