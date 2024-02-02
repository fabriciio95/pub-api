CREATE TABLE categoria_unidade_conversao (
    categoria_id BIGINT NOT NULL,
    unidade_conversao_id BIGINT NOT NULL,
    CONSTRAINT pk_categoria_unidade_conversao PRIMARY KEY (categoria_id, unidade_conversao_id),
    CONSTRAINT fk_categoria_unidade_conversao_categoria FOREIGN KEY (categoria_id) REFERENCES public.categoria (id),
    CONSTRAINT fk_categoria_unidade_conversao_unidade_conversao FOREIGN KEY (unidade_conversao_id) REFERENCES public.unidade_conversao (id)
);
