package com.gestao.homologacao.controllers;

import com.gestao.homologacao.entities.Partner;
import com.gestao.homologacao.repositories.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partners")
public class PartnerController {

    @Autowired
    private PartnerRepository repository;

    @GetMapping
    public List<Partner> listAll() {
        return repository.findAll();
    }

    @PostMapping
    public Partner create(@RequestBody Partner partner){
        return repository.save(partner);
    }
}
