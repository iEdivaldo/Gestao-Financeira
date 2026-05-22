package com.gestaofinanceira.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestaofinanceira.backend.model.Orcamento;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, UUID> {
    List<Orcamento> findAllByUsuarioIdAndMesAndAno(UUID usuarioId, int mes, int ano);

    boolean existsByCategoriaIdAndMesAndAnoAndUsuarioId(UUID categoriaId, int mes, int ano, UUID usuarioId);

    Optional<Orcamento> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
