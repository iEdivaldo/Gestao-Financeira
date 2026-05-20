package com.gestaofinanceira.backend.dto.Usuario;

import com.gestaofinanceira.backend.model.enums.Perfil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    private String nome;
    private String email;
    private String senha;
    private Perfil perfil;
}
