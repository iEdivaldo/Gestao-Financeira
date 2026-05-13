package com.gestaofinanceira.backend.dto.Orcamento;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrcamentoDTO {

    private UUID id;
    private Integer mes;
    private Integer ano;
    private Double valorLimite;
    private UUID categoriaId;
    private UUID usuarioId;
    private Double percentualUtilizado;
}
