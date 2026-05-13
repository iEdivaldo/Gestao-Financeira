package com.gestaofinanceira.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestaofinanceira.backend.model.Usuario;

public interface UsuarioRepository extends  JpaRepository<Usuario, UUID> {
    Usuario findByEmail(String email);
}
