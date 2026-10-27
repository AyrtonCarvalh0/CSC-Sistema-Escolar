CREATE TABLE aviso (
                       id          UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
                       titulo      VARCHAR(255),
                       conteudo    TEXT,
                       criado_em   TIMESTAMP,
                       expira_em   TIMESTAMP,
                       turma_id    UUID        REFERENCES turma(id),
                       aluno_id    UUID        REFERENCES aluno(id)
);
