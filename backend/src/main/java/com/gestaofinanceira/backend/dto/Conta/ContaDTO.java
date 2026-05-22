package com.gestaofinanceira.backend.dto.Conta;

import java.math.BigDecimal;
import java.util.UUID;

import com.gestaofinanceira.backend.model.Conta;
import com.gestaofinanceira.backend.model.enums.TipoConta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ContaDTO (

    UUID id,

    @NotBlank(message = "O nome da conta é obrigatório.")
    String nome,

    @NotNull(message = "O tipo da conta é obrigatório.")
    TipoConta tipo,

    @NotNull(message = "O saldo inicial da conta é obrigatório.")
    @PositiveOrZero(message = "O saldo inicial não pode ser negativo.")
    BigDecimal saldoInicial,

    BigDecimal saldoAtual,
    String cor,
    Boolean ativo
) {
    public static ContaDTO de(Conta conta) {
        return new ContaDTO(
            conta.getId(),
            conta.getNome(),
            conta.getTipo(),
            conta.getSaldoInicial(),
            conta.getSaldoAtual(),
            conta.getCor(),
            conta.getAtivo()
        );
    }
}
