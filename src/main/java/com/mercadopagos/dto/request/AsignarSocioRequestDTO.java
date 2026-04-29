package com.mercadopagos.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignarSocioRequestDTO {

    @NotNull(message = "El ID del socio es obligatorio")
    private Long socioId;
}