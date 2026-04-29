package com.mercadopagos.mapper;

import com.mercadopagos.dto.response.DeudaResponseDTO;
import com.mercadopagos.entity.Deuda;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeudaMapper {

    public DeudaResponseDTO toDTO(Deuda deuda) {
        return new DeudaResponseDTO(
                deuda.getId(),
                deuda.getPuesto().getId(),
                deuda.getPuesto().getCodigo(),
                deuda.getSocio().getId(),
                deuda.getSocio().getNombres() + " " + deuda.getSocio().getApellidos(),
                deuda.getConcepto(),
                deuda.getMonto(),
                deuda.getFechaEmision(),
                deuda.getFechaVencimiento(),
                deuda.getEstado(),
                deuda.getDescripcion()
        );
    }

    public List<DeudaResponseDTO> toDTOList(List<Deuda> deudas) {
        List<DeudaResponseDTO> resultado = new ArrayList<>();
        for (Deuda deuda : deudas) {
            resultado.add(toDTO(deuda));
        }
        return resultado;
    }
}