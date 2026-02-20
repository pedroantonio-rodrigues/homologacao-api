package com.gestao.homologacao.repositories;

import com.gestao.homologacao.entities.PartnerEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartnerEmployeeRepository extends JpaRepository<PartnerEmployee, Long> {


    List<PartnerEmployee> findByPartnerId(Long partnerId);

    Optional<PartnerEmployee> findByCpf(String cpf);

}
