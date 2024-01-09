CREATE TABLE unidade (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL
);

CREATE TABLE unidade_conversao (
    id BIGSERIAL PRIMARY KEY,
    unidade_origem BIGINT NOT NULL,
    unidade_destino BIGINT NOT NULL,
    fator_conversao DOUBLE PRECISION NOT NULL,
    
    FOREIGN KEY (unidade_origem) REFERENCES unidade(id),
    FOREIGN KEY (unidade_destino) REFERENCES unidade(id)
);

ALTER TABLE produto
ADD COLUMN unidade_id BIGINT,
ADD CONSTRAINT fk_produto_unidade FOREIGN KEY (unidade_id) REFERENCES unidade(id);
