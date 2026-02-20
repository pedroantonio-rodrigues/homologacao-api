package com.gestao.homologacao.repositories;

import com.gestao.homologacao.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {


    Optional<Partner> findByCnpj( String cnpj);
}
