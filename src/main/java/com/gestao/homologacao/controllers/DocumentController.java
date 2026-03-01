package com.gestao.homologacao.controllers;


import com.gestao.homologacao.entities.Document;
import com.gestao.homologacao.entities.DocumentHistory;
import com.gestao.homologacao.enums.DocumentStatus;
import com.gestao.homologacao.repositories.DocumentHistoryRepository;
import com.gestao.homologacao.repositories.DocumentRepository;
import com.gestao.homologacao.repositories.DocumentTypeRepository;
import com.gestao.homologacao.repositories.PartnerEmployeeRepository;
import com.gestao.homologacao.services.DocumentService;
import com.gestao.homologacao.services.FileStorageService;
import com.gestao.homologacao.services.PartnerEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    private DocumentHistoryRepository documentHistoryRepository;

    @Autowired
    private FileStorageService fileService;

    @Autowired
    private PartnerEmployeeService partnerEmployeeService;

    @Autowired
    private DocumentService documentService;


    // Metodo para subir um arquivo "Documento"
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
        doc.setDocumentType(documentTypeRepository.findById(typeId).orElseThrow());

        return documentRepository.save(doc);
    }

   // Lista todos os documentos de um funcionario especifico
    @GetMapping("/employee/{employeeId}")
    public List<Document> getByEmployee(@PathVariable Long employeeId){
        return documentRepository.findByEmployeeId(employeeId);
    }

    // Atualiza o status e faz a verificação da homologação
    @PatchMapping("/{id}/status")
    public Document updateStatus (
            @PathVariable Long id,
            @RequestParam(name = "status") DocumentStatus documentStatus,
            @RequestParam(required = false) String observation) {

        return documentRepository.findById(id).map(doc -> {
            doc.setStatus(documentStatus);
            doc.setObservation(observation);
            Document saveDoc = documentRepository.save(doc);

            DocumentHistory log = new DocumentHistory();
            log.setDocument(saveDoc);
            log.setStatusAtTime(documentStatus);
            log.setObservation(observation);
            documentHistoryRepository.save(log);

            // Checa se o funcionario enviou todos os documentos obrigatorios
            partnerEmployeeService.checkHomologation(saveDoc.getEmployee().getId());

            return saveDoc;
        }).orElseThrow(() -> new RuntimeException("Documento não encontrado"));
    }

    // Metodo para deletar um Documento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id){
        // Busca o documento no banco de dados e pega o nome do arquivo (filepath)
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        // Chama o serviço para carregar o arquivo fisico
        Resource resource = fileService.loadFile(doc.getFilePath());

        // Define o tipo de conteudo (PDF, imagem, etc)
        String contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                // faz o navegador tentar abrir o PDF em vez de apenas baixar
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }




}
