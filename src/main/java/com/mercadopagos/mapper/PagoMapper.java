package com.mercadopagos.mapper;

import com.mercadopagos.dto.response.PagoResponseDTO;
import com.mercadopagos.entity.Pago;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PagoMapper {

    public PagoResponseDTO toDTO(Pago pago) {
        return new PagoResponseDTO(
                pago.getId(),
                pago.getDeuda().getId(),
                pago.getDeuda().getConcepto(),
                pago.getDeuda().getPuesto().getId(),
                pago.getDeuda().getPuesto().getCodigo(),
                pago.getDeuda().getSocio().getId(),
                pago.getDeuda().getSocio().getNombres() + " " + pago.getDeuda().getSocio().getApellidos(),
                pago.getMonto(),
                pago.getFechaPago()
        );
    }

    public List<PagoResponseDTO> toDTOList(List<Pago> pagos) {
        List<PagoResponseDTO> resultado = new ArrayList<>();
        for (Pago pago : pagos) {
            resultado.add(toDTO(pago));
        }
        return resultado;
    }
}