package com.mercadopagos.service;

import com.mercadopagos.dto.request.SocioRequestDTO;
import com.mercadopagos.dto.response.PuestoResponseDTO;
import com.mercadopagos.dto.response.SocioResponseDTO;

import java.util.List;

public interface SocioService {

    List<SocioResponseDTO> listarTodos();

    SocioResponseDTO obtenerPorId(Long id);

    SocioResponseDTO crear(SocioRequestDTO dto);

    SocioResponseDTO actualizar(Long id, SocioRequestDTO dto);

    void eliminar(Long id);

    SocioResponseDTO bloquear(Long id, String observacion);

    SocioResponseDTO desbloquear(Long id);

    List<PuestoResponseDTO> listarPuestosDeSocio(Long socioId);
}