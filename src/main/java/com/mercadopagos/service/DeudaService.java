package com.mercadopagos.service;

import com.mercadopagos.dto.request.DeudaMultipleRequestDTO;
import com.mercadopagos.dto.request.DeudaRequestDTO;
import com.mercadopagos.dto.response.DeudaResponseDTO;
import com.mercadopagos.dto.response.MorosidadDTO;
import com.mercadopagos.dto.response.ResumenCobranzaDTO;

import java.util.List;

public interface DeudaService {

    DeudaResponseDTO crearDeuda(DeudaRequestDTO dto);

    List<DeudaResponseDTO> crearDeudasMultiple(DeudaMultipleRequestDTO dto);

    List<DeudaResponseDTO> listarPendientes();

    List<DeudaResponseDTO> listarPagadas();

    List<DeudaResponseDTO> listarVencidas();

    List<DeudaResponseDTO> listarPorPuesto(Long puestoId);

    List<DeudaResponseDTO> listarSinPuesto();

    List<MorosidadDTO> reporteMorosidad();

    ResumenCobranzaDTO obtenerResumenCobranzas();
}