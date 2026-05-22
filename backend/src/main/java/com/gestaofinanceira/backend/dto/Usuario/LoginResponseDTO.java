package com.gestaofinanceira.backend.dto.Usuario;

import java.util.UUID;

import com.gestaofinanceira.backend.model.enums.Perfil;

public record LoginResponseDTO (

    String token,
    UUID id,
    String nome,
    String email,
    Perfil perfil
) {}
