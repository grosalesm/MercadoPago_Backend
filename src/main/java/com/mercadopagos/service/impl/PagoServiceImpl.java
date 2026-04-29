package com.mercadopagos.service.impl;

import com.mercadopagos.dto.request.PagoRequestDTO;
import com.mercadopagos.dto.response.PagoResponseDTO;
import com.mercadopagos.entity.Deuda;
import com.mercadopagos.entity.Pago;
import com.mercadopagos.entity.Puesto;
import com.mercadopagos.enums.EstadoDeuda;
import com.mercadopagos.exception.ResourceNotFoundException;
import com.mercadopagos.mapper.PagoMapper;
import com.mercadopagos.repository.DeudaRepository;
import com.mercadopagos.repository.PagoRepository;
import com.mercadopagos.repository.PuestoRepository;
import com.mercadopagos.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final DeudaRepository deudaRepository;
    private final PuestoRepository puestoRepository;
    private final PagoMapper pagoMapper;

    @Override
    public PagoResponseDTO pagarDeudaIndividual(PagoRequestDTO dto) {
        Deuda deuda = deudaRepository.findById(dto.deudaId())
                .orElseThrow(() -> new ResourceNotFoundException("Deuda no encontrada con ID: " + dto.deudaId()));

        if (deuda.getEstado() == EstadoDeuda.PAGADO) {
            throw new IllegalStateException("La deuda ya ha sido pagada.");
        }

        Pago pago = new Pago();
        pago.setDeuda(deuda);
        pago.setFechaPago(LocalDate.now());
        pago.setMonto(deuda.getMonto());

        pago = pagoRepository.save(pago);

        deuda.setEstado(EstadoDeuda.PAGADO);
        deudaRepository.save(deuda);

        return pagoMapper.toDTO(pago);
    }

    @Override
    public List<PagoResponseDTO> pagarTotalPuesto(Long puestoId) {
        Puesto puesto = puestoRepository.findById(puestoId)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + puestoId));

        List<EstadoDeuda> estadosPendientes = new ArrayList<>();
        estadosPendientes.add(EstadoDeuda.PENDIENTE);
        estadosPendientes.add(EstadoDeuda.VENCIDO);

        List<Deuda> deudasPendientes = deudaRepository.findByPuestoAndEstadoIn(puesto, estadosPendientes);

        if (deudasPendientes.isEmpty()) {
            throw new IllegalStateException("El puesto no tiene deudas pendientes.");
        }

        List<PagoResponseDTO> pagosRealizados = new ArrayList<>();
        for (Deuda deuda : deudasPendientes) {
            Pago pago = new Pago();
            pago.setDeuda(deuda);
            pago.setFechaPago(LocalDate.now());
            pago.setMonto(deuda.getMonto());

            pago = pagoRepository.save(pago);

            deuda.setEstado(EstadoDeuda.PAGADO);
            deudaRepository.save(deuda);

            pagosRealizados.add(pagoMapper.toDTO(pago));
        }

        return pagosRealizados;
    }

    @Override
    public Double obtenerTotalPagadoHoy() {
        LocalDate hoy = LocalDate.now();
        return pagoRepository.sumMontoByFechaPago(hoy);
    }
}