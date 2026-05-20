package com.gestaofinanceira.backend.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestaofinanceira.backend.dto.LogsAuditorias.RelatorioMensalDTO;
import com.gestaofinanceira.backend.dto.Transacao.TransacaoResponseDTO;
import com.gestaofinanceira.backend.model.Transacao;
import com.gestaofinanceira.backend.model.enums.StatusTransacao;
import com.gestaofinanceira.backend.model.enums.TipoTransacao;
import com.gestaofinanceira.backend.repository.TransacaoRepository;

@Service
public class RelatorioService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    public RelatorioMensalDTO gerarRelatorioMensalPorCategoria(Integer mes, Integer ano) {
        if (mes == null || ano == null) {
            throw new IllegalArgumentException("Mês e ano são obrigatórios para gerar o relatório.");
        }

        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês inválido. O valor deve estar entre 1 e 12.");
        }

        Date inicio = criarDataInicioDoMes(mes, ano);
        Date fim = criarDataFimDoMes(mes, ano);

        List<Transacao> transacoes = transacaoRepository.findByDataVencimentoBetween(inicio, fim);
        List<TransacaoResponseDTO> transacoesPorCategoria = new ArrayList<>();

        double totalReceitas = 0.0;
        double totalDespesas = 0.0;

        for (Transacao transacao : transacoes) {
            if (transacao.getStatus() == StatusTransacao.CANCELADO) {
                continue;
            }

            if (transacao.getTipo() == TipoTransacao.RECEITA) {
                totalReceitas += transacao.getValor();
            } else if (transacao.getTipo() == TipoTransacao.DESPESA) {
                totalDespesas += transacao.getValor();
            }

            transacoesPorCategoria.add(criarTransacaoResponseDTO(transacao));
        }

        return RelatorioMensalDTO.builder()
                .totalReceitas(totalReceitas)
                .totalDespesas(totalDespesas)
                .saldoFinal(totalReceitas - totalDespesas)
                .transacoesPorCategoria(transacoesPorCategoria)
                .build();
    }

    private Date criarDataInicioDoMes(Integer mes, Integer ano) {
        Calendar data = Calendar.getInstance();
        data.clear();
        data.set(Calendar.YEAR, ano);
        data.set(Calendar.MONTH, mes - 1);
        data.set(Calendar.DAY_OF_MONTH, 1);
        data.set(Calendar.HOUR_OF_DAY, 0);
        data.set(Calendar.MINUTE, 0);
        data.set(Calendar.SECOND, 0);
        data.set(Calendar.MILLISECOND, 0);
        return data.getTime();
    }

    private Date criarDataFimDoMes(Integer mes, Integer ano) {
        Calendar data = Calendar.getInstance();
        data.clear();
        data.set(Calendar.YEAR, ano);
        data.set(Calendar.MONTH, mes - 1);
        data.set(Calendar.DAY_OF_MONTH, data.getActualMaximum(Calendar.DAY_OF_MONTH));
        data.set(Calendar.HOUR_OF_DAY, 23);
        data.set(Calendar.MINUTE, 59);
        data.set(Calendar.SECOND, 59);
        data.set(Calendar.MILLISECOND, 999);
        return data.getTime();
    }

    private TransacaoResponseDTO criarTransacaoResponseDTO(Transacao transacao) {
        return TransacaoResponseDTO.builder()
                .id(transacao.getId())
                .descricao(transacao.getDescricao())
                .valor(transacao.getValor())
                .tipo(transacao.getTipo())
                .status(transacao.getStatus())
                .dataVencimento(transacao.getDataVencimento())
                .dataPagamento(transacao.getDataPagamento())
                .recorrente(transacao.getRecorrente())
                .observacao(transacao.getObservacao())
                .usuarioId(transacao.getUsuario() != null ? transacao.getUsuario().getId() : null)
                .categoriaId(transacao.getCategoria() != null ? transacao.getCategoria().getId() : null)
                .contaId(transacao.getConta() != null ? transacao.getConta().getId() : null)
                .criadoEm(transacao.getCriadoEm())
                .nomeCategoria(transacao.getCategoria() != null ? transacao.getCategoria().getNome() : null)
                .nomeConta(transacao.getConta() != null ? transacao.getConta().getNome() : null)
                .build();
    }
}
