CREATE TABLE responsavel (
    id          UUID PRIMARY KEY,
    name        VARCHAR(255),
    cpf         VARCHAR(255) UNIQUE,
    endereco    VARCHAR(255),
    telefone    VARCHAR(255),
    email       VARCHAR(255)
);

CREATE TABLE professor (
    id          UUID PRIMARY KEY,
    name        VARCHAR(255),
    cpf         VARCHAR(255),
    telefone    VARCHAR(255),
    email       VARCHAR(255)
);

CREATE TABLE turma (
    id           UUID PRIMARY KEY,
    nome         VARCHAR(255),
    idade_max    INTEGER,
    idade_min    INTEGER,
    capacidade   INTEGER,
    professor_id UUID REFERENCES professor(id)
);

CREATE TABLE aluno (
    id               UUID PRIMARY KEY,
    nome             VARCHAR(255),
    cpf              VARCHAR(255) UNIQUE,
    responsavel_id   UUID REFERENCES responsavel(id),
    turma            VARCHAR(255),
    data_nascimento  DATE
);

CREATE TABLE pagamento (
    id              UUID PRIMARY KEY,
    aluno_id        UUID REFERENCES aluno(id),
    mes             VARCHAR(255),
    valor           DOUBLE PRECISION,
    pago            BOOLEAN,
    data_pagamento  TIMESTAMP
);

CREATE TABLE matricula (
    id        UUID PRIMARY KEY,
    aluno_id  UUID REFERENCES aluno(id),
    turma_id  UUID REFERENCES turma(id)
);

CREATE TABLE usuarios (
    id     UUID PRIMARY KEY,
    login  VARCHAR(255),
    senha  VARCHAR(255),
    role   VARCHAR(255)
);
