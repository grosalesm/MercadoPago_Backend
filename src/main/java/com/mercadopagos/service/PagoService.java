package com.mercadopagos.service;

import com.mercadopagos.dto.request.PagoRequestDTO;
import com.mercadopagos.dto.response.PagoResponseDTO;

import java.util.List;

public interface PagoService {

    PagoResponseDTO pagarDeudaIndividual(PagoRequestDTO dto);
    List<PagoResponseDTO> pagarTotalPuesto(Long puestoId);
    Double obtenerTotalPagadoHoy();
}