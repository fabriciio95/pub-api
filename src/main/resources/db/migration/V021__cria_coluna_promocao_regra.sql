ALTER TABLE regra_promocao
ADD COLUMN promocao_id BIGINT NOT NULL;

ALTER TABLE regra_promocao
ADD CONSTRAINT fk_promocao_regra
FOREIGN KEY (promocao_id)
REFERENCES promocao (id);
