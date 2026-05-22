package com.gestaofinanceira.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestaofinanceira.backend.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, UUID> {
    List<Conta> findAllByUsuarioId(UUID usuario);

    List<Conta> findAllByUsuarioIdAndAtivo(UUID usuario, boolean ativo);

    Optional<Conta> findByIdAndUsuarioId(UUID id, UUID usuario);
}
