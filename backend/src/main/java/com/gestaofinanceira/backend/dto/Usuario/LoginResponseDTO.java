package com.gestaofinanceira.backend.dto.Usuario;

import java.util.UUID;

import com.gestaofinanceira.backend.model.ClassEnum.Perfil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private UUID id;
    private String nome;
    private String email;
    private Perfil perfil;
}
