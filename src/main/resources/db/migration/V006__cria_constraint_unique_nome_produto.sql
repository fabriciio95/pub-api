ALTER TABLE produto
ADD CONSTRAINT nome_unidade_unico UNIQUE (nome, unidade_id);
