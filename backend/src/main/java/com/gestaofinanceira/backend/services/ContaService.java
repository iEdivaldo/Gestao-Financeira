package com.gestaofinanceira.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestaofinanceira.backend.model.Conta;
import com.gestaofinanceira.backend.repository.ContaRepository;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    //CRUD
    public List<Conta> listarTodasContas() {
        return contaRepository.findAll();
    }

    public Conta criarConta(Conta conta) {
        return contaRepository.save(conta);
    }

    public Conta atualizarSaldoDaConta(Conta conta) {
        return contaRepository.save(conta);
    }
}
