package com.mercadopagos.mapper;

import com.mercadopagos.dto.response.SocioResponseDTO;
import com.mercadopagos.entity.Socio;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SocioMapper {

    public SocioResponseDTO toDTO(Socio socio) {
        SocioResponseDTO dto = new SocioResponseDTO();
        dto.setId(socio.getId());
        dto.setNombres(socio.getNombres());
        dto.setApellidos(socio.getApellidos());
        dto.setNombreCompleto(socio.getNombres() + " " + socio.getApellidos());
        dto.setDni(socio.getDni());
        dto.setTelefono(socio.getTelefono());
        dto.setEmail(socio.getEmail());
        dto.setActivo(socio.getActivo());
        dto.setObservacion(socio.getObservacion());
        dto.setFechaRegistro(socio.getFechaRegistro());
        return dto;
    }

    public List<SocioResponseDTO> toDTOList(List<Socio> socios) {
        List<SocioResponseDTO> resultado = new ArrayList<>();
        for (Socio socio : socios) {
            resultado.add(toDTO(socio));
        }
        return resultado;
    }
}