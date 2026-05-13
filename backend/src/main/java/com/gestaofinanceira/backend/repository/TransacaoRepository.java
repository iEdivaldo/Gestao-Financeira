package com.gestaofinanceira.backend.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestaofinanceira.backend.model.Categoria;
import com.gestaofinanceira.backend.model.Conta;
import com.gestaofinanceira.backend.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {
    List<Transacao> findByDataBetweenAndStatusAndCategoriaAndConta(Date inicio, Date fim, String status, Categoria categoria, Conta conta);
}
