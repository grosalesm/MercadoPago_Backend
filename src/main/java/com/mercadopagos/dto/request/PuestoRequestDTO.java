package com.mercadopagos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PuestoRequestDTO {

    @NotBlank(message = "El código del puesto es obligatorio")
    @Size(max = 20)
    private String codigo;

    @Size(max = 200)
    private String descripcion;
}