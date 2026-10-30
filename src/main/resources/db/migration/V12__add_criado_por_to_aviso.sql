ALTER TABLE aviso
    ADD COLUMN IF NOT EXISTS criado_por_id UUID REFERENCES usuarios(id);
