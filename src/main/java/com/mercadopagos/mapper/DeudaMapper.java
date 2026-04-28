package com.mercadopagos.mapper;

import com.mercadopagos.dto.response.DeudaResponseDTO;
import com.mercadopagos.entity.Deuda;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeudaMapper {

    public DeudaResponseDTO toDTO(Deuda deuda) {
        DeudaResponseDTO dto = new DeudaResponseDTO();
        dto.setId(deuda.getId());
        dto.setPuestoId(deuda.getPuesto().getId());
        dto.setCodigoPuesto(deuda.getPuesto().getCodigo());
        dto.setSocioId(deuda.getSocio().getId());
        dto.setSocioNombreCompleto(deuda.getSocio().getNombres() + " " + deuda.getSocio().getApellidos());
        dto.setConcepto(deuda.getConcepto());
        dto.setMonto(deuda.getMonto());
        dto.setFechaEmision(deuda.getFechaEmision());
        dto.setFechaVencimiento(deuda.getFechaVencimiento());
        dto.setEstado(deuda.getEstado());
        dto.setDescripcion(deuda.getDescripcion());
        return dto;
    }

    public List<DeudaResponseDTO> toDTOList(List<Deuda> deudas) {
        List<DeudaResponseDTO> resultado = new ArrayList<>();
        for (Deuda deuda : deudas) {
            resultado.add(toDTO(deuda));
        }
        return resultado;
    }
}