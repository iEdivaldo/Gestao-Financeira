package com.gestaofinanceira.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestaofinanceira.backend.model.Categoria;
import com.gestaofinanceira.backend.model.enums.TipoTransacao;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    List<Categoria> findAllByUsuarioIdAndAtivo(UUID usuarioId, boolean ativo);

    List<Categoria> findAllByUsuarioIdAndTipoAndAtivo(UUID usuarioId, TipoTransacao tipo, boolean ativo);

    boolean existsByNomeAndUsuarioId(String nome, UUID usuarioId);

    Optional<Categoria> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
