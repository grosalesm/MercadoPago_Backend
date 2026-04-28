package com.mercadopagos.mapper;

import com.mercadopagos.dto.response.PagoResponseDTO;
import com.mercadopagos.entity.Pago;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PagoMapper {

    public PagoResponseDTO toDTO(Pago pago) {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setId(pago.getId());
        dto.setDeudaId(pago.getDeuda().getId());
        dto.setConcepto(pago.getDeuda().getConcepto());
        dto.setPuestoId(pago.getDeuda().getPuesto().getId());
        dto.setCodigoPuesto(pago.getDeuda().getPuesto().getCodigo());
        dto.setSocioId(pago.getDeuda().getSocio().getId());
        dto.setSocioNombreCompleto(pago.getDeuda().getSocio().getNombres() + " " + pago.getDeuda().getSocio().getApellidos());
        dto.setMonto(pago.getMonto());
        dto.setFechaPago(pago.getFechaPago());
        return dto;
    }

    public List<PagoResponseDTO> toDTOList(List<Pago> pagos) {
        List<PagoResponseDTO> resultado = new ArrayList<>();
        for (Pago pago : pagos) {
            resultado.add(toDTO(pago));
        }
        return resultado;
    }
}