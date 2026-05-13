package com.gestaofinanceira.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestaofinanceira.backend.model.Conta;
import com.gestaofinanceira.backend.model.Usuario;

public interface ContaRepository extends JpaRepository<Conta, UUID> {
    Conta findAllByUsuario(Usuario usuario);
}
