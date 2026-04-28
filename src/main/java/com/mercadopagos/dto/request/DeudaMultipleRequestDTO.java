package com.mercadopagos.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeudaMultipleRequestDTO {

    @NotEmpty(message = "Debe incluir al menos una deuda")
    @Valid
    private List<DeudaRequestDTO> deudas;
}