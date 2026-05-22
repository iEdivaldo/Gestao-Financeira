package com.gestaofinanceira.backend.dto.Categoria;

import java.util.UUID;

import com.gestaofinanceira.backend.model.Categoria;
import com.gestaofinanceira.backend.model.enums.TipoTransacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaDTO (

    UUID id,

    @NotBlank(message = "O nome é obrigatório")
    String nome,

    @NotNull(message = "O tipo de transação é obrigatório")
    TipoTransacao tipo,

    String cor,
    String icone,
    Boolean ativo
) {
    public static CategoriaDTO de(Categoria categoria) {
        return new CategoriaDTO(
            categoria.getId(),
            categoria.getNome(),
            categoria.getTipo(),
            categoria.getCor(),
            categoria.getIcone(),
            categoria.getAtivo()
        );
    }
}
