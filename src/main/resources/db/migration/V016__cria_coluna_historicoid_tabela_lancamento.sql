ALTER TABLE lancamento
ADD COLUMN historico_produto_id BIGINT,
ADD CONSTRAINT fk_lancamento_historico_produto FOREIGN KEY (historico_produto_id) REFERENCES historico_produto(id);
