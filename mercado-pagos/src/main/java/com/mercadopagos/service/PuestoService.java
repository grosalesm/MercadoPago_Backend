package com.mercadopagos.service;

import com.mercadopagos.dto.request.AsignarSocioRequestDTO;
import com.mercadopagos.dto.request.PuestoRequestDTO;
import com.mercadopagos.dto.response.PuestoEstadisticasDTO;
import com.mercadopagos.dto.response.PuestoResponseDTO;

import java.util.List;

public interface PuestoService {

    List<PuestoResponseDTO> listarTodos();

    PuestoResponseDTO obtenerPorId(Long id);

    PuestoResponseDTO crear(PuestoRequestDTO dto);

    PuestoResponseDTO actualizar(Long id, PuestoRequestDTO dto);

    void eliminar(Long id);

    List<PuestoResponseDTO> listarOcupados();

    List<PuestoResponseDTO> listarLibres();

    PuestoEstadisticasDTO obtenerEstadisticas();

    PuestoResponseDTO asignarSocio(Long puestoId, AsignarSocioRequestDTO dto);

    PuestoResponseDTO liberarPuesto(Long puestoId);
}