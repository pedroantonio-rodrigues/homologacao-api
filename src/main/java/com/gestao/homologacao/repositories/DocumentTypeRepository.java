package com.gestao.homologacao.repositories;

import com.gestao.homologacao.entities.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
}
