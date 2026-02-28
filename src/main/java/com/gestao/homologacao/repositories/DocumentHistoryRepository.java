package com.gestao.homologacao.repositories;

import com.gestao.homologacao.entities.DocumentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {
}
