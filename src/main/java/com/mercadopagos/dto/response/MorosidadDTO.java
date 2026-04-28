package com.mercadopagos.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class MorosidadDTO {

    private Long puestoId;
    private String codigoPuesto;
    private Long socioId;
    private String socioNombreCompleto;
    private List<DeudaResponseDTO> deudasVencidas;
    private BigDecimal totalMora;
}