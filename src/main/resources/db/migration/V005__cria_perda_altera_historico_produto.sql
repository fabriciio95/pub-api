CREATE TABLE perda_avaria (
    id BIGSERIAL PRIMARY KEY,
    motivo VARCHAR(255) NOT NULL,
    quantidade INTEGER NOT NULL,
    data TIMESTAMP WITH TIME ZONE NOT NULL,
    unidade_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,

    FOREIGN KEY (unidade_id) REFERENCES unidade(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);


ALTER TABLE historico_produto
ADD COLUMN perda_avaria_id BIGINT,
ADD CONSTRAINT fk_historico_produto_perda_avaria FOREIGN KEY (perda_avaria_id) REFERENCES perda_avaria(id);
