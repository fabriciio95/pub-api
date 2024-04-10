CREATE TABLE evento (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    data_hora_inicio_evento TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    data_hora_fim_evento TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    data_cadastro TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);