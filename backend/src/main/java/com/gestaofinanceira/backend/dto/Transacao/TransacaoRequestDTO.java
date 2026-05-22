package com.gestaofinanceira.backend.dto.Transacao;

import java.time.LocalDate;
import java.util.UUID;

import com.gestaofinanceira.backend.model.enums.StatusTransacao;
import com.gestaofinanceira.backend.model.enums.TipoTransacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record TransacaoRequestDTO (

    @NotBlank(message = "A descrição da transação é obrigatória.")
    String descricao,

    @NotNull(message = "O valor da transação é obrigatório.")
    @PositiveOrZero(message = "O valor da transação não pode ser negativo.")
    String valor,

    @NotNull(message = "O tipo da transação é obrigatório.")
    TipoTransacao tipo,

    @NotNull(message = "O status da transação é obrigatório.")
    StatusTransacao status,

    @NotNull(message = "A data de vencimento da transação é obrigatória.")
    LocalDate dataVencimento,

    LocalDate dataPagamento,
    Boolean recorrente,
    String observacao,

    @NotNull(message = "O ID da categoria é obrigatório.")
    UUID categoriaId,
    
    @NotNull(message = "O ID da conta é obrigatório.")
    UUID contaId
) {}