CREATE TABLE boletim (
                         id           UUID    NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
                         aluno_id     UUID    REFERENCES aluno(id),
                         disciplina   VARCHAR(255),
                         nota         DOUBLE PRECISION,
                         bimestre     INTEGER,
                         ano          INTEGER,
                         lancado_em   TIMESTAMP
);
