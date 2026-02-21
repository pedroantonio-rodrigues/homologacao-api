package com.gestao.homologacao.controllers;


import com.gestao.homologacao.entities.Document;
import com.gestao.homologacao.enums.DocumentStatus;
import com.gestao.homologacao.repositories.DocumentRepository;
import com.gestao.homologacao.repositories.DocumentTypeRepository;
import com.gestao.homologacao.repositories.PartnerEmployeeRepository;
import com.gestao.homologacao.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private PartnerEmployeeRepository partnerEmployeeRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private FileStorageService fileService;

    @PostMapping("/upload")
    public Document upload(
            @RequestParam Long employeeId,
            @RequestParam Long typeId,
            @RequestParam("file")MultipartFile file
            ){
        // 1. Salva o arquivo fisico e pega o nome gerado
        String fileName = fileService.save(file);

        // Cria um objeto Document vinculado
        Document doc = new Document();
        doc.setFilePath(fileName);
        doc.setStatus(DocumentStatus.UNDER_REVIEW); // Envia o documento para a revisão

        // Busca as entidades no banco para garantir o vinculo
        doc.setEmployee(partnerEmployeeRepository.findById(employeeId).orElseThrow());
        doc.setType(documentTypeRepository.findById(typeId).orElseThrow());

        return documentRepository.save(doc);
    }

}
