package com.gestaofinanceira.backend.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestaofinanceira.backend.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {
    List<Transacao> findByDataVencimentoBetween(Date inicio, Date fim);
}
