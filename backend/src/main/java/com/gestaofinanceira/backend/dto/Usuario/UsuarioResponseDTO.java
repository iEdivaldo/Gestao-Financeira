package com.gestaofinanceira.backend.dto.Usuario;

import java.time.LocalDateTime;
import java.util.UUID;

import com.gestaofinanceira.backend.model.Usuario;
import com.gestaofinanceira.backend.model.enums.Perfil;

public record  UsuarioResponseDTO (

    UUID id,
    String nome,
    String email,
    Boolean ativo,
    Perfil perfil,
    LocalDateTime criadoEm
) {
    public static UsuarioResponseDTO de(Usuario usuario) {
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getAtivo(),
            usuario.getPerfil(),
            usuario.getCriadoEm()
        );
    }
}