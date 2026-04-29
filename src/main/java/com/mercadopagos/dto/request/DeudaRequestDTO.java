package com.mercadopagos.dto.request;

import com.mercadopagos.enums.ConceptoDeuda;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DeudaRequestDTO(
        @NotNull(message = "El ID del puesto es obligatorio") Long puestoId,
        @NotNull(message = "El concepto de la deuda es obligatorio") ConceptoDeuda concepto,
        @NotNull(message = "El monto es obligatorio") @Positive(message = "El monto debe ser mayor a cero") BigDecimal monto,
        @NotNull(message = "La fecha de emisión es obligatoria") LocalDate fechaEmision,
        @NotNull(message = "La fecha de vencimiento es obligatoria") LocalDate fechaVencimiento,
        @Size(max = 300) String descripcion
) {}