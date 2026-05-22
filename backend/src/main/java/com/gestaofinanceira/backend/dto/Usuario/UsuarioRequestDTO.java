package com.gestaofinanceira.backend.dto.Usuario;

import com.gestaofinanceira.backend.model.enums.Perfil;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO (

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve conter entre 2 e 100 caracteres")
    String nome,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    String email,

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve conter no mínimo 6 caracteres")
    String senha,
    
    @NotBlank(message = "O perfil é obrigatório")
    Perfil perfil
) {}
