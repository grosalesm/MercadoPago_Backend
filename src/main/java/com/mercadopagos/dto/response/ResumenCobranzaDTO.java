package com.mercadopagos.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ResumenCobranzaDTO {

    private BigDecimal totalPendienteGeneral;
    private long totalDeudasPendientes;
    private long totalPuestosConDeuda;
    private long totalDeudoresSinPuesto;
    private List<SocioResponseDTO> deudoresSinPuesto;
}