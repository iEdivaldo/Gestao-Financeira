CREATE TABLE logs_auditorias (
    id UUID PRIMARY KEY,
    acao VARCHAR(50) NOT NULL,
    entidade VARCHAR(50),
    entidade_id UUID,
    detalhe TEXT,
    ip VARCHAR(45),
    usuario_id UUID NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_log_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
);