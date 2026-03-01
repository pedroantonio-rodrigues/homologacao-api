package com.gestao.homologacao.controllers;

import com.gestao.homologacao.dto.EmployeeApprovalRequestDTO;
import com.gestao.homologacao.dto.PartnerEmployeeRequestDTO;
import com.gestao.homologacao.dto.PartnerEmployeeResponseDTO;
import com.gestao.homologacao.entities.PartnerEmployee;
import com.gestao.homologacao.enums.EmployeeStatus;
import com.gestao.homologacao.repositories.PartnerEmployeeRepository;
import com.gestao.homologacao.repositories.PartnerRepository;
import com.gestao.homologacao.services.PartnerEmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class PartnerEmployeeController {

    @Autowired
    private PartnerEmployeeRepository partnerEmployeeRepository;

    @Autowired
    private PartnerEmployeeService partnerEmployeeService;

    @Autowired
    private PartnerRepository partnerRepository;


    @PostMapping("/{partnerId}")
    public ResponseEntity<PartnerEmployeeResponseDTO> create(@PathVariable Long partnerId,
                                                             @RequestBody @Valid PartnerEmployeeRequestDTO dto){

        // Converte o DTO em entidade para salvar
        PartnerEmployee employee = new PartnerEmployee();
        employee.setName(dto.getName());
        employee.setCpf(dto.getCpf());
        employee.setStatus(EmployeeStatus.PENDING_DOCUMENTS); // Status de inicio padrão


        PartnerEmployee savedEmployee = partnerRepository.findById(partnerId)
                .map(partnerEmployee -> {
                    employee.setPartner(partnerEmployee);
                    return partnerEmployeeRepository.save(employee);
                }).orElseThrow(() -> new RuntimeException("Partner não encontrado com o ID " + partnerId));

                // Retorna o DTO em vez da entidade
                return ResponseEntity.ok(partnerEmployeeService.convertToResponseDTO(savedEmployee));
    }

    @GetMapping
    public ResponseEntity<List<PartnerEmployeeResponseDTO>> list(@RequestParam(required = false)EmployeeStatus status) {
        List<PartnerEmployee> employees;
        if (status != null) {
            employees = partnerEmployeeRepository.findByStatus(status);
        } else {
            employees = partnerEmployeeRepository.findAll();
        }

        List<PartnerEmployeeResponseDTO> dtos = employees.stream()
                .map(partnerEmployeeService::convertToResponseDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PartnerEmployeeResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid EmployeeApprovalRequestDTO dto) {

        // Chama o serviço de atualizar o status do funcionario
        PartnerEmployeeResponseDTO response = partnerEmployeeService.updateStatus(id, dto);

        return ResponseEntity.ok(response);
    }


}
