ALTER TABLE aviso
    ADD COLUMN criado_por_id UUID REFERENCES usuarios(id);ALTER TABLE aviso
    ADD COLUMN criado_por_id UUID REFERENCES usuarios(id);
