package com.mercadopagos.dto.response;

import com.mercadopagos.enums.EstadoPuesto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PuestoResponseDTO {

    private Long id;
    private String codigo;
    private String descripcion;
    private EstadoPuesto estado;
    private Long socioId;
    private String socioNombreCompleto;
    private String socioDni;
}