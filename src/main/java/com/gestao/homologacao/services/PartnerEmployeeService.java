package com.gestao.homologacao.services;

import com.gestao.homologacao.entities.Document;
import com.gestao.homologacao.entities.PartnerEmployee;
import com.gestao.homologacao.enums.DocumentStatus;
import com.gestao.homologacao.enums.EmployeeStatus;
import com.gestao.homologacao.repositories.DocumentRepository;
import com.gestao.homologacao.repositories.PartnerEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
