package com.mercadopagos.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record MorosidadDTO(
        Long puestoId,
        String codigoPuesto,
        Long socioId,
        String socioNombreCompleto,
        List<DeudaResponseDTO> deudasVencidas,
        BigDecimal totalMora
) {}