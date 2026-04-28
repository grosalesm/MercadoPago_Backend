package com.mercadopagos.dto.response;

import com.mercadopagos.enums.ConceptoDeuda;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PagoResponseDTO {

    private Long id;
    private Long deudaId;
    private ConceptoDeuda concepto;
    private Long puestoId;
    private String codigoPuesto;
    private Long socioId;
    private String socioNombreCompleto;
    private BigDecimal monto;
    private LocalDate fechaPago;
}