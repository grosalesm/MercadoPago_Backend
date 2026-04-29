package com.mercadopagos.dto.response;

import java.time.LocalDate;

public record SocioResponseDTO(
        Long id,
        String nombres,
        String apellidos,
        String nombreCompleto,
        String dni,
        String telefono,
        String email,
        Boolean activo,
        String observacion,
        LocalDate fechaRegistro
) {}