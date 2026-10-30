package com.eiba.System_Finances.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Payload para criação de um aviso.
 *
 * Regras de escopo:
 *  - turmaId e alunoId nulos  → aviso geral (para toda a escola)
 *  - turmaId preenchido       → aviso para uma turma específica
 *  - alunoId preenchido       → aviso para um aluno específico
 */
public record CriarAvisoRequestDTO(
        String titulo,
        String conteudo,
        LocalDateTime expiraEm,
        UUID turmaId,
        UUID alunoId
) {}
