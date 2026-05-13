package com.gestaofinanceira.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestaofinanceira.backend.model.Orcamento;
import com.gestaofinanceira.backend.model.Usuario;

public interface OrcamentoRepository extends JpaRepository<Orcamento, UUID> {
    Orcamento findByMesAndAnoAndUsuario(int mes, int ano, Usuario usuario);
}
