package com.gestaofinanceira.backend.dto.Usuario;

import java.time.LocalDateTime;

import com.gestaofinanceira.backend.model.ClassEnum.Perfil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private Boolean ativo;
    private Perfil perfil;
    private LocalDateTime criadoEm;
}
