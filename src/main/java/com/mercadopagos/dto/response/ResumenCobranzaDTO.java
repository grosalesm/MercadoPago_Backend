package com.mercadopagos.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ResumenCobranzaDTO(
        BigDecimal totalPendienteGeneral,
        long totalDeudasPendientes,
        long totalPuestosConDeuda,
        long totalDeudoresSinPuesto,
        List<SocioResponseDTO> deudoresSinPuesto
) {}