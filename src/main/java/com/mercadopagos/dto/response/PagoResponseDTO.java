package com.mercadopagos.dto.response;

import com.mercadopagos.enums.ConceptoDeuda;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PagoResponseDTO(
        Long id,
        Long deudaId,
        ConceptoDeuda concepto,
        Long puestoId,
        String codigoPuesto,
        Long socioId,
        String socioNombreCompleto,
        BigDecimal monto,
        LocalDate fechaPago
) {}