package com.eiba.System_Finances.DTO.portal;

import java.util.UUID;

public record PendenciaDTO(
        UUID id,
        String mes,
        Double valor
) {}
