package com.gestao.homologacao.repositories;

import com.gestao.homologacao.entities.PartnerEmployee;
import com.gestao.homologacao.enums.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartnerEmployeeRepository extends JpaRepository<PartnerEmployee, Long> {


    // Metodo para encontrar o funcionario do parceiro pelo id
    List<PartnerEmployee> findByPartnerId(Long partnerId);

    // Metodo para encontar o funcionario do parceiro pelo numero do CPF
    Optional<PartnerEmployee> findByCpf(String cpf);

    // Metodo para econtrar os funcionarios do parceiro pelo status
    List<PartnerEmployee> findByStatus(EmployeeStatus status);
}
