package com.mercadopagos.dto.response;

public record PuestoEstadisticasDTO(
        long total,
        long ocupados,
        long libres,
        long inhabilitados
) {}