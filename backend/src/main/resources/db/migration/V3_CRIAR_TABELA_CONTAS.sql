CREATE TABLE contas (
    id UUID PRIMARY KEY,
    nome VARCHAR(80) NOT NULL,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('CORRENTE', 'POUPANCA', 'CARTEIRA', 'INVESTIMENTO')),
    saldo_inicial DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    saldo_atual DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    cor VARCHAR(7) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    usuario_id UUID NOT NULL,

    CONSTRAINT fk_conta_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);