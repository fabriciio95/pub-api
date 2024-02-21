ALTER TABLE historico_produto RENAME COLUMN preco TO valor_total;

ALTER TABLE historico_produto ADD COLUMN IF NOT EXISTS valor_unitario NUMERIC;

ALTER TABLE historico_produto RENAME COLUMN quantidade TO quantidade_transacao;

ALTER TABLE historico_produto ADD COLUMN IF NOT EXISTS quantidade_estoque INTEGER;

