package com.mercadopagos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SocioRequestDTO(
        @NotBlank(message = "Los nombres son obligatorios") @Size(max = 100) String nombres,
        @NotBlank(message = "Los apellidos son obligatorios") @Size(max = 100) String apellidos,
        @NotBlank(message = "El DNI es obligatorio") @Size(min = 8, max = 20, message = "El DNI debe tener entre 8 y 20 caracteres") String dni,
        @Size(max = 20) String telefono,
        @Size(max = 150) String email
) {}