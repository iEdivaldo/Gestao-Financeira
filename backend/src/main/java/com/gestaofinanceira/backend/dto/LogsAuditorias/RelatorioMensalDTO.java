package com.gestaofinanceira.backend.dto.LogsAuditorias;

import java.math.BigDecimal;
import java.util.List;

public record RelatorioMensalDTO (

    int mes,
    int ano,
    BigDecimal totalReceitas,
    BigDecimal totalDespesas,
    BigDecimal saldo,
    BigDecimal totalPendente,
    List<ResumoPorCategoriaDTO> despesasPorCategoria
) {
    public record ResumoPorCategoriaDTO (
        String categoriaNome,
        String categoriaIcone,
        String categoriaCor,
        BigDecimal total,
        BigDecimal percentualDoTotal
    ) {}
}
