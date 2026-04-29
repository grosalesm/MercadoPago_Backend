package com.mercadopagos.dto.response;

import com.mercadopagos.enums.ConceptoDeuda;
import com.mercadopagos.enums.EstadoDeuda;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DeudaResponseDTO(
        Long id,
        Long puestoId,
        String codigoPuesto,
        Long socioId,
        String socioNombreCompleto,
        ConceptoDeuda concepto,
        BigDecimal monto,
        LocalDate fechaEmision,
        LocalDate fechaVencimiento,
        EstadoDeuda estado,
        String descripcion
) {}