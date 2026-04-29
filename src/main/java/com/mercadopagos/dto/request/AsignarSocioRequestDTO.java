package com.mercadopagos.dto.request;

import jakarta.validation.constraints.NotNull;

public record AsignarSocioRequestDTO(
        @NotNull(message = "El ID del socio es obligatorio") Long socioId
) {}