CREATE TABLE categorias (
    id UUID PRIMARY KEY AUTO_GENERATED,
    nome VARCHAR(80) NOT NULL,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('RECEITA', 'DESPESA')),
    cor VARCHAR(7) NOT NULL,
    icone VARCHAR(50) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    usuario_id UUID NOT NULL,

    CONSTRAINT fk_categoria_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);