package com.gestaofinanceira.backend.dto.Conta;

import com.gestaofinanceira.backend.model.enums.TipoConta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaDTO {

    private String id;
    private String nome;
    private TipoConta tipo;
    private String saldoInicial;
    private String saldoAtual;
    private String cor;
    private Boolean ativo; 
}
