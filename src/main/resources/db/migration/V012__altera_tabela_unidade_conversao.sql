ALTER TABLE unidade_conversao DROP CONSTRAINT IF EXISTS unidade_conversao_unidade_destino_fkey;
ALTER TABLE unidade_conversao DROP CONSTRAINT IF EXISTS unidade_conversao_unidade_origem_fkey;

ALTER TABLE unidade_conversao DROP COLUMN IF EXISTS unidade_origem;
ALTER TABLE unidade_conversao DROP COLUMN IF EXISTS unidade_destino;

ALTER TABLE unidade_conversao ADD COLUMN IF NOT EXISTS descricao_origem VARCHAR(255) NOT NULL;
