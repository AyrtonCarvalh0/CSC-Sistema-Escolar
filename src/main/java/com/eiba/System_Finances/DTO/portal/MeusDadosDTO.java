package com.eiba.System_Finances.DTO.portal;

import java.time.LocalDate;
import java.util.UUID;

public record MeusDadosDTO(
        UUID id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String turma,
        String responsavel
) {}
