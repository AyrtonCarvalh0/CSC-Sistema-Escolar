package com.eiba.System_Finances.DTO.portal;

import java.time.LocalDateTime;
import java.util.UUID;

public record AvisoDTO(
        UUID id,
        String titulo,
        String conteudo,
        LocalDateTime criadoEm,
        LocalDateTime expiraEm
) {}
