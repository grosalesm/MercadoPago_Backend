package com.mercadopagos.mapper;

import com.mercadopagos.dto.response.SocioResponseDTO;
import com.mercadopagos.entity.Socio;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SocioMapper {

    public SocioResponseDTO toDTO(Socio socio) {
        return new SocioResponseDTO(
                socio.getId(),
                socio.getNombres(),
                socio.getApellidos(),
                socio.getNombres() + " " + socio.getApellidos(),
                socio.getDni(),
                socio.getTelefono(),
                socio.getEmail(),
                socio.getActivo(),
                socio.getObservacion(),
                socio.getFechaRegistro()
        );
    }

    public List<SocioResponseDTO> toDTOList(List<Socio> socios) {
        List<SocioResponseDTO> resultado = new ArrayList<>();
        for (Socio socio : socios) {
            resultado.add(toDTO(socio));
        }
        return resultado;
    }
}