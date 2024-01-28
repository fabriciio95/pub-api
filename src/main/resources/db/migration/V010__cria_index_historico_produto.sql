CREATE INDEX idx_historico_produto_produto_id ON public.historico_produto USING btree (produto_id);
CREATE INDEX idx_historico_produto_tipo_transacao ON public.historico_produto USING btree (tipo_transacao);
CREATE INDEX idx_historico_produto_data ON public.historico_produto USING btree ("data");
