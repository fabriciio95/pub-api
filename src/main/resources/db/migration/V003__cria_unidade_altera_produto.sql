CREATE TABLE unidade (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL
);

CREATE TABLE unidade_conversao (
    id BIGSERIAL PRIMARY KEY,
    unidade_origem BIGINT,
    unidade_destino BIGINT,
    fator_conversao DOUBLE PRECISION,
    
    FOREIGN KEY (unidade_origem) REFERENCES unidade(id),
    FOREIGN KEY (unidade_destino) REFERENCES unidade(id)
);

ALTER TABLE produto
ADD COLUMN unidade_id BIGINT,
ADD CONSTRAINT fk_produto_unidade FOREIGN KEY (unidade_id) REFERENCES unidade(id);
