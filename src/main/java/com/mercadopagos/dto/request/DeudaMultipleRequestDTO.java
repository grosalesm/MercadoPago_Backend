package com.mercadopagos.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record DeudaMultipleRequestDTO(
        @NotEmpty(message = "Debe incluir al menos una deuda") @Valid List<DeudaRequestDTO> deudas
) {}