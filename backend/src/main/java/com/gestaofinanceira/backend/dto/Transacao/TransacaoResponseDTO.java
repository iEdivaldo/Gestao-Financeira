package com.gestaofinanceira.backend.dto.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.gestaofinanceira.backend.model.Transacao;
import com.gestaofinanceira.backend.model.enums.StatusTransacao;
import com.gestaofinanceira.backend.model.enums.TipoTransacao;

public record TransacaoResponseDTO (

    UUID id,
    String descricao,
    BigDecimal valor,
    TipoTransacao tipo,
    StatusTransacao status,
    LocalDate dataVencimento,
    LocalDate dataPagamento,
    Boolean recorrente,
    String observacao,

    UUID categoriaId,
    String categoriaNome,
    String categoriaIcone,
    String categoriaCor,

    UUID contaId,
    String contaNome,

    LocalDateTime criadoEm
) {
    public static TransacaoResponseDTO de(Transacao transacao) {
        return new TransacaoResponseDTO(
            transacao.getId(),
            transacao.getDescricao(),
            transacao.getValor(),
            transacao.getTipo(),
            transacao.getStatus(),
            transacao.getDataVencimento(),
            transacao.getDataPagamento(),
            transacao.getRecorrente(),
            transacao.getObservacao(),
            transacao.getCategoria().getId(),
            transacao.getCategoria().getNome(),
            transacao.getCategoria().getIcone(),
            transacao.getCategoria().getCor(),
            transacao.getConta().getId(),
            transacao.getConta().getNome(),
            transacao.getCriadoEm()
        );
    }
}
