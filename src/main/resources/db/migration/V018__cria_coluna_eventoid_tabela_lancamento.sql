ALTER TABLE lancamento
ADD COLUMN evento_id BIGINT,
ADD CONSTRAINT fk_lancamento_evento FOREIGN KEY (evento_id) REFERENCES evento(id);
