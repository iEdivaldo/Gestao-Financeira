package com.gestaofinanceira.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestaofinanceira.backend.model.Orcamento;
import com.gestaofinanceira.backend.repository.OrcamentoRepository;

@Service
public class OrcamentoService {

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    public Orcamento salvaOrcamento(Orcamento orcamento) {
        return orcamentoRepository.save(orcamento);
    } 

    public double verificarPercentualGastoDoMes(Orcamento orcamento) {
        if (orcamento.getValorLimite() > 0) {
            return (orcamento.getValorLimite() / orcamento.getValorLimite()) * 100;
        }
        return 0;
    }
}
