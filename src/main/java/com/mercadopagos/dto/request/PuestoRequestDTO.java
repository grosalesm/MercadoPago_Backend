package com.mercadopagos.dto.request;

import com.mercadopagos.enums.TipoPuesto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PuestoRequestDTO(
        @NotBlank(message = "El código del puesto es obligatorio") @Size(max = 20) String codigo,
        @Size(max = 200) String descripcion,
        TipoPuesto tipo
) {}