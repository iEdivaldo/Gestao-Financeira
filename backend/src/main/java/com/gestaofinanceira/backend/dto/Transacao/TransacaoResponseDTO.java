package com.gestaofinanceira.backend.dto.Transacao;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import com.gestaofinanceira.backend.model.ClassEnum.StatusTransacao;
import com.gestaofinanceira.backend.model.ClassEnum.TipoTransacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoResponseDTO {

    private UUID id;
    private String descricao;
    private Double valor;
    private TipoTransacao tipo;
    private StatusTransacao status;
    private Date dataVencimento;
    private Date dataPagamento;
    private Boolean recorrente;
    private String observacao;
    private UUID usuarioId;
    private UUID categoriaId;
    private UUID contaId;
    private LocalDateTime criadoEm;
    
    private String nomeCategoria;
    private String nomeConta;
}
