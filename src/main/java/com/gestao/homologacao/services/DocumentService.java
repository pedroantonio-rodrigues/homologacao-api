package com.gestao.homologacao.services;


import com.gestao.homologacao.entities.Document;
import com.gestao.homologacao.enums.EmployeeStatus;
import com.gestao.homologacao.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public void deleteDocument(Long id){
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado com o ID: " + id));

        if (doc.getEmployee().getStatus() == EmployeeStatus.HOMOLOGATED){
            throw new IllegalStateException("Ação Bloqueada: Não é possivel remover documentos de um funcionario já HOMOLOGADO.");
        }

        documentRepository.delete(doc);
    }
}
