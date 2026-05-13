package com.gestaofinanceira.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestaofinanceira.backend.model.LogAuditoria;
import com.gestaofinanceira.backend.model.Usuario;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, UUID> {
    LogAuditoria findByUsuarioAndDataBetween(Usuario usuario, String dataInicio, String dataFim);
}
