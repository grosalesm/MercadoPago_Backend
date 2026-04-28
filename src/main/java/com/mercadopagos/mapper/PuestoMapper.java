package com.mercadopagos.mapper;

import com.mercadopagos.dto.response.PuestoResponseDTO;
import com.mercadopagos.entity.Puesto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PuestoMapper {

    public PuestoResponseDTO toDTO(Puesto puesto) {
        PuestoResponseDTO dto = new PuestoResponseDTO();
        dto.setId(puesto.getId());
        dto.setCodigo(puesto.getCodigo());
        dto.setDescripcion(puesto.getDescripcion());
        dto.setEstado(puesto.getEstado());

        if (puesto.getSocio() != null) {
            dto.setSocioId(puesto.getSocio().getId());
            dto.setSocioNombreCompleto(puesto.getSocio().getNombres() + " " + puesto.getSocio().getApellidos());
            dto.setSocioDni(puesto.getSocio().getDni());
        }

        return dto;
    }

    public List<PuestoResponseDTO> toDTOList(List<Puesto> puestos) {
        List<PuestoResponseDTO> resultado = new ArrayList<>();
        for (Puesto puesto : puestos) {
            resultado.add(toDTO(puesto));
        }
        return resultado;
    }
}