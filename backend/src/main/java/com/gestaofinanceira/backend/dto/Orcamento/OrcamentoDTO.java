package com.gestaofinanceira.backend.dto.Orcamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import com.gestaofinanceira.backend.model.Orcamento;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrcamentoDTO (

    UUID id,

    @NotNull @Min(1) @Max(12)
    Integer mes,

    @NotNull @Min(2020)
    Integer ano,

    @NotNull @Positive
    BigDecimal valorLimite,

    @NotNull(message = "O ID da categoria é obrigatório.")
    UUID categoriaId,
    String categoriaNome,

    BigDecimal percentualUtilizado,

    BigDecimal valorGasto
) {
    public static OrcamentoDTO de(Orcamento o, BigDecimal valorGasto) {
        BigDecimal percentual = o.getValorLimite().compareTo(BigDecimal.ZERO) == 0
            ? BigDecimal.ZERO : 
            // calcula: (gasto / limite) * 100, com 2 casas decimais
            valorGasto.multiply(BigDecimal.valueOf(100)).divide(o.getValorLimite(), 2, RoundingMode.HALF_UP);

        return new OrcamentoDTO(
            o.getId(),
            o.getMes(),
            o.getAno(),
            o.getValorLimite(),
            o.getCategoria().getId(),
            o.getCategoria().getNome(),
            percentual,
            valorGasto
        );
    }
}
