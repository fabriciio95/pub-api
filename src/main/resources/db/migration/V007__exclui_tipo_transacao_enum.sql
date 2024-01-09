ALTER TABLE historico_produto ALTER COLUMN tipo_transacao TYPE VARCHAR(20) USING tipo_transacao::VARCHAR;

DROP TYPE IF EXISTS tipo_transacao;
