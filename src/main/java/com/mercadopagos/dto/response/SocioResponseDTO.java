package com.mercadopagos.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SocioResponseDTO {

    private Long id;
    private String nombres;
    private String apellidos;
    private String nombreCompleto;
    private String dni;
    private String telefono;
    private String email;
    private Boolean activo;
    private String observacion;
    private LocalDate fechaRegistro;
}