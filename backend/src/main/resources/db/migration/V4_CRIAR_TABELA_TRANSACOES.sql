CREATE TABLE transacoes (
    id UUID PRIMARY KEY AUTO_GENERATED,
    descricao VARCHAR(200) NOT NULL,
    valor DECIMAL(15, 2) NOT NULL CHECK (valor > 0),
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('RECEITA', 'DESPESA')),
    status VARCHAR(15) NOT NULL CHECK (status IN ('PENDENTE', 'PAGO', 'RECEBIDO', 'CANCELADO')),
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    recorrente BOOLEAN NOT NULL DEFAULT FALSE,
    observacao TEXT,

    conta_id UUID NOT NULL,
    categoria_id UUID NOT NULL,
    usuario_id UUID NOT NULL,

    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transacao_conta FOREIGN KEY (conta_id) REFERENCES contas(id),
    CONSTRAINT fk_transacao_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    CONSTRAINT fk_transacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);