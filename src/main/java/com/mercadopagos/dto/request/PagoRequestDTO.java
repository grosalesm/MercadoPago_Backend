package com.mercadopagos.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagoRequestDTO {

    @NotNull(message = "El ID de la deuda es obligatorio")
    private Long deudaId;
}