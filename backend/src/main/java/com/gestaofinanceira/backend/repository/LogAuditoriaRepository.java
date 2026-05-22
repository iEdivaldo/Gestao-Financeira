package com.gestaofinanceira.backend.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestaofinanceira.backend.model.LogAuditoria;

@Repository
public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, UUID> {
    // Pageable permite paginar os resultados, ordenando por data de criação em ordem decrescente
    Page<LogAuditoria> findAllByUsuarioIdOrderByCriadoEmDesc(UUID usuarioId, Pageable pageable);
    
    // Buscar logs de auditoria por usuário e intervalo de datas, ordenando por data de criação em ordem decrescente
    Page<LogAuditoria> findAllByUsuarioIdAndCriadoEmBetweenOrderByCriadoEmDesc(UUID usuarioId, LocalDateTime inicio, LocalDateTime fim, Pageable pageable);
}
