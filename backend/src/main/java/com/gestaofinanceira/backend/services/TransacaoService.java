package com.gestaofinanceira.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestaofinanceira.backend.model.Conta;
import com.gestaofinanceira.backend.model.Transacao;
import com.gestaofinanceira.backend.model.enums.StatusTransacao;
import com.gestaofinanceira.backend.model.enums.TipoTransacao;
import com.gestaofinanceira.backend.repository.TransacaoRepository;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    public Transacao criarTransacao(Transacao transacao) {
        return transacaoRepository.save(transacao);
    }

    public Transacao atualizarSaldoConta(Transacao transacao) {
        return transacaoRepository.save(transacao);
    }

    public void calcularValoresTotais(Transacao transacao) {
        if (transacao == null 
                || transacao.getConta() == null 
                || transacao.getValor() == null
                || transacao.getStatus() == null
                || transacao.getTipo() == null) {
            return;
        }

        Conta conta = transacao.getConta();
        Double saldoAtual = conta.getSaldoAtual() != null ? conta.getSaldoAtual() : 0.0;
        
        if (transacao.getStatus() == StatusTransacao.PAGO || transacao.getStatus() == StatusTransacao.RECEBIDO) {
            Double ajuste = transacao.getTipo() == TipoTransacao.RECEITA 
                    ? transacao.getValor() 
                    : -transacao.getValor();

            conta.setSaldoAtual(saldoAtual + ajuste);
        }
    }
}
