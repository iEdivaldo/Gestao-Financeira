package com.gestaofinanceira.backend.dto.LogsAuditorias;

import java.util.List;

import com.gestaofinanceira.backend.dto.Transacao.TransacaoResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioMensalDTO {

    private Double totalReceitas;
    private Double totalDespesas;
    private Double saldoFinal;
    private List<TransacaoResponseDTO> transacoesPorCategoria;
}
