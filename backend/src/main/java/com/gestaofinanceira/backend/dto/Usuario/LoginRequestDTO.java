package com.gestaofinanceira.backend.dto.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    String email,

    @NotBlank(message = "A senha é obrigatória")
    String senha
) {}
