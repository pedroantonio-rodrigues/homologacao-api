package com.gestao.homologacao.services;

import com.gestao.homologacao.dto.DocumentResponseDTO;
import com.gestao.homologacao.dto.EmployeeApprovalRequestDTO;
import com.gestao.homologacao.dto.PartnerEmployeeHistoryResponseDTO;
import com.gestao.homologacao.dto.PartnerEmployeeResponseDTO;
import com.gestao.homologacao.entities.Document;
import com.gestao.homologacao.entities.PartnerEmployee;
import com.gestao.homologacao.entities.PartnerEmployeeHistory;
import com.gestao.homologacao.enums.DocumentStatus;
import com.gestao.homologacao.enums.EmployeeStatus;
import com.gestao.homologacao.repositories.DocumentRepository;
import com.gestao.homologacao.repositories.PartnerEmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PartnerEmployeeService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private PartnerEmployeeRepository partnerEmployeeRepository;

    public void checkHomologation( Long employeeId){
        // Lista todos os documentos envidados do funcionario
        List<Document> docs = documentRepository.findByEmployeeId(employeeId);

        // IDs dos tipos de documentos obrigatorios definidos
        List<Long> requiredTypes = Arrays.asList(1L,2L);

        // Verifica se para cada tipo obrigatorio existe um documento APROVADO
        boolean isEligible = requiredTypes.stream().allMatch(typeId -> docs.stream().anyMatch(d ->
                d.getDocumentType().getId().equals(typeId) &&
                d.getStatus() == DocumentStatus.VALID));
        if (isEligible){
            PartnerEmployee employee = partnerEmployeeRepository.findById(employeeId).orElseThrow();
            employee.setStatus(EmployeeStatus.HOMOLOGATED);
            partnerEmployeeRepository.save(employee);
        }
    }

    public PartnerEmployeeResponseDTO convertToResponseDTO(PartnerEmployee partnerEmployee){
        PartnerEmployeeResponseDTO dto = new PartnerEmployeeResponseDTO();

        // Dados basicos dos funcionarios
        dto.setId(partnerEmployee.getId());
        dto.setName(partnerEmployee.getName());
        dto.setCpf(partnerEmployee.getCpf());
        dto.setStatus(partnerEmployee.getStatus().toString());


        // Mapeando a lista de historicos
        if (partnerEmployee.getPartnerEmployeeHistories() != null){

            List<PartnerEmployeeHistoryResponseDTO> historyDTOs = partnerEmployee.getPartnerEmployeeHistories()
                    .stream()
                    .map( h -> {
                        PartnerEmployeeHistoryResponseDTO hDto = new PartnerEmployeeHistoryResponseDTO();
                        hDto.setId(h.getId());
                        hDto.setOldStatus(h.getOldStatus());
                        hDto.setNewStatus(h.getNewStatus());
                        hDto.setChangeDate(h.getChangeDate());
                        hDto.setObservation(h.getObservation());

                        return hDto;
                    }).toList();
            dto.setHistories(historyDTOs);
        }

        // Pegando apenas o nome da empresa parceira
        if (partnerEmployee.getPartner() != null){
            dto.setPartnerName(partnerEmployee.getPartner().getCompanyName());
        }

        // Convertendo a lista de entidades Document para a lista de DTOs
        if (partnerEmployee.getDocuments() != null){
            List<DocumentResponseDTO> docDTOs = partnerEmployee.getDocuments().stream()
                    .map(doc -> {
                        DocumentResponseDTO d = new DocumentResponseDTO();
                        d.setId(doc.getId());
                        d.setFileName(doc.getFileName());
                        d.setStatus(doc.getStatus().toString());
                        d.setTypeName(doc.getDocumentType().getName()); // nome do tipo de documento
                        d.setUploadDate(doc.getUploadDate());
                        return d;
                    })
                    .toList();
            dto.setDocuments(docDTOs);
        }
        return dto;
    }

    @Transactional
    public PartnerEmployeeResponseDTO updateStatus(Long id, EmployeeApprovalRequestDTO dto){
        PartnerEmployee employee = partnerEmployeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));

        // Regra de negocio
        if (dto.getStatus() == EmployeeStatus.ACTIVE && employee.getStatus() != EmployeeStatus.HOMOLOGATED){
            throw new RuntimeException("Não é possivel ativar um funcionario que não foi homologado! ");
        }

        // Cria o registro do historico
        PartnerEmployeeHistory history = new PartnerEmployeeHistory();
        history.setOldStatus(employee.getStatus()); // Como era o status
        history.setNewStatus(dto.getStatus()); // Como vai ser o status
        history.setChangeDate(LocalDateTime.now());
        history.setObservation(dto.getObservation());
        history.setPartnerEmployee(employee);

        // Adiciona o historico na lista do funcionario
        employee.getPartnerEmployeeHistories().add(history);

        // Atualiza o status atual do funcionario
        employee.setStatus(dto.getStatus());

        employee.setObservation(dto.getObservation());

        PartnerEmployee savedEmployee = partnerEmployeeRepository.save(employee);
        return convertToResponseDTO(savedEmployee);

    }


}
