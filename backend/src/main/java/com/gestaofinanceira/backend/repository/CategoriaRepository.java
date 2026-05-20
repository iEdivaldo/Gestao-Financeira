package com.gestaofinanceira.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestaofinanceira.backend.model.Categoria;
import com.gestaofinanceira.backend.model.Usuario;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    List<Categoria> findAllByUsuarioAndAtivo(Usuario usuario, boolean ativo);
}
