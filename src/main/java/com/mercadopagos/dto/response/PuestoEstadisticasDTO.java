package com.mercadopagos.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PuestoEstadisticasDTO {

    private long total;
    private long ocupados;
    private long libres;
    private long inhabilitados;
}