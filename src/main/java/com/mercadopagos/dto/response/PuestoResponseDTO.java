package com.mercadopagos.dto.response;

import com.mercadopagos.enums.EstadoPuesto;
import com.mercadopagos.enums.TipoPuesto;

public record PuestoResponseDTO(
        Long id,
        String codigo,
        String descripcion,
        EstadoPuesto estado,
        TipoPuesto tipo,
        Long socioId,
        String socioNombreCompleto,
        String socioDni
) {}