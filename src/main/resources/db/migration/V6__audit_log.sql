CREATE TABLE IF NOT EXISTS audit_log (
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario   VARCHAR(255) NOT NULL,
    acao      VARCHAR(100) NOT NULL,
    entidade  VARCHAR(100) NOT NULL,
    detalhe   TEXT,
    ip        VARCHAR(50),
    data_hora TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_audit_usuario   ON audit_log(usuario);
CREATE INDEX idx_audit_data_hora ON audit_log(data_hora DESC);
CREATE INDEX idx_audit_acao      ON audit_log(acao);
