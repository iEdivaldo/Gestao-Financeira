CREATE TABLE orcamentos (
    id UUID PRIMARY KEY,
    mes INTEGER NOT NULL CHECK (mes >= 1 AND mes <= 12),
    ano INTEGER NOT NULL CHECK (ano >= 2000),
    valor_limite DECIMAL(15, 2) NOT NULL CHECK (valor > 0),

    categoria_id UUID NOT NULL,
    usuario_id UUID NOT NULL,

    CONSTRAINT fk_orcamento_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_orcamento_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    
    CONSTRAINT uk_orcamento_categoria_mes_ano_usuario UNIQUE (categoria_id, mes, ano, usuario_id)
);