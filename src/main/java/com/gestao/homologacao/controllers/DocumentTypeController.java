package com.gestao.homologacao.controllers;


import com.gestao.homologacao.entities.DocumentType;
import com.gestao.homologacao.repositories.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/document-types")
public class DocumentTypeController {

    @Autowired
    private DocumentTypeRepository repository;

    @PostMapping
    public DocumentType create(@RequestBody DocumentType documentType){
        return repository.save(documentType);
    }

    @GetMapping
    public List<DocumentType> listAll(){
        return repository.findAll();
    }
}
