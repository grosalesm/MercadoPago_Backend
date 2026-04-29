package com.mercadopagos.mapper;

import com.mercadopagos.dto.response.PuestoResponseDTO;
import com.mercadopagos.entity.Puesto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PuestoMapper {

    public PuestoResponseDTO toDTO(Puesto puesto) {
        return new PuestoResponseDTO(
                puesto.getId(),
                puesto.getCodigo(),
                puesto.getDescripcion(),
                puesto.getEstado(),
                puesto.getTipo(),
                puesto.getSocio() != null ? puesto.getSocio().getId() : null,
                puesto.getSocio() != null ? puesto.getSocio().getNombres() + " " + puesto.getSocio().getApellidos() : null,
                puesto.getSocio() != null ? puesto.getSocio().getDni() : null
        );
    }

    public List<PuestoResponseDTO> toDTOList(List<Puesto> puestos) {
        List<PuestoResponseDTO> resultado = new ArrayList<>();
        for (Puesto puesto : puestos) {
            resultado.add(toDTO(puesto));
        }
        return resultado;
    }
}