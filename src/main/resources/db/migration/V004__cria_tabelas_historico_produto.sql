CREATE TABLE historico_preco_produto (
    id BIGSERIAL PRIMARY KEY,
    data TIMESTAMP WITH TIME ZONE,
    preco NUMERIC,
    produto_id BIGINT,

    FOREIGN KEY (produto_id) REFERENCES produto(id)
);


CREATE TYPE tipo_transacao AS ENUM ('VENDA', 'COMPRA', 'PERDA'); 

CREATE TABLE historico_produto (
    id BIGSERIAL PRIMARY KEY,
    data TIMESTAMP WITH TIME ZONE,
    preco NUMERIC,
    quantidade INTEGER,
    tipo_transacao tipo_transacao,
    produto_id BIGINT,
    unidade_id BIGINT,

    FOREIGN KEY (produto_id) REFERENCES produto(id),
    FOREIGN KEY (unidade_id) REFERENCES unidade(id)
);
