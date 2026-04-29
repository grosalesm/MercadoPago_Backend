package com.mercadopagos.dto.request;

import com.mercadopagos.enums.ConceptoDeuda;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DeudaRequestDTO {

    @NotNull(message = "El ID del puesto es obligatorio")
    private Long puestoId;

    @NotNull(message = "El concepto de la deuda es obligatorio")
    private ConceptoDeuda concepto;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal monto;

    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDate fechaEmision;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fechaVencimiento;

    @Size(max = 300)
    private String descripcion;
}