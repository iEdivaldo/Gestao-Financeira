package com.gestaofinanceira.backend.dto.Categoria;

import com.gestaofinanceira.backend.model.enums.TipoTransacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {

    private String id;
    private String nome;
    private TipoTransacao tipo;
    private String cor;
    private String icone;
    private Boolean ativo;
}
