package com.gestao.homologacao.controllers;

import com.gestao.homologacao.entities.PartnerEmployee;
import com.gestao.homologacao.repositories.PartnerEmployeeRepository;
import com.gestao.homologacao.repositories.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class PartnerEmployeeController {

    @Autowired
    private PartnerEmployeeRepository employeeRepository;

    @Autowired
    private PartnerRepository partnerRepository;


    @PostMapping("/{partnerId}")
    public PartnerEmployee create(@PathVariable Long partnerId, @RequestBody PartnerEmployee employee){
        // Busca o parceiro pelo ID da URL
        return partnerRepository.findById(partnerId).map(partner -> {
            //Vincula parceiro á um funcionario
            employee.setPartner(partner);
            // Salva o funcionario
            return employeeRepository.save(employee);
        }).orElseThrow(() -> new RuntimeException("Partener não encontrado com o ID " + partnerId));
    }

    @GetMapping
    public List<PartnerEmployee> listAll(){
        return employeeRepository.findAll();
    }
}
