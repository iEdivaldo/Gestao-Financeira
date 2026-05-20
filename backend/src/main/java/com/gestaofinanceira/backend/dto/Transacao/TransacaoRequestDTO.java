package com.gestaofinanceira.backend.dto.Transacao;

import java.util.Date;
import java.util.UUID;

import com.gestaofinanceira.backend.model.enums.StatusTransacao;
import com.gestaofinanceira.backend.model.enums.TipoTransacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoRequestDTO {

    private String descricao;
    private String valor;
    private TipoTransacao tipo;
    private StatusTransacao status;
    private Date dataVencimento;
    private UUID categoriaId;
    private UUID contaId; 
}
