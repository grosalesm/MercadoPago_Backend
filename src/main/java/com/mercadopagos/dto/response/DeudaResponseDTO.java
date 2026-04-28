package com.mercadopagos.dto.response;

import com.mercadopagos.enums.ConceptoDeuda;
import com.mercadopagos.enums.EstadoDeuda;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DeudaResponseDTO {

    private Long id;
    private Long puestoId;
    private String codigoPuesto;
    private Long socioId;
    private String socioNombreCompleto;
    private ConceptoDeuda concepto;
    private BigDecimal monto;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private EstadoDeuda estado;
    private String descripcion;
}