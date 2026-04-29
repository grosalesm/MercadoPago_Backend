package com.mercadopagos.dto.request;

import jakarta.validation.constraints.NotNull;

public record PagoRequestDTO(
        @NotNull(message = "El ID de la deuda es obligatorio") Long deudaId
) {}