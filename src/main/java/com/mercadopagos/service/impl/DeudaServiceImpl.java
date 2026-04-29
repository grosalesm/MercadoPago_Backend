package com.mercadopagos.service.impl;

import com.mercadopagos.dto.request.DeudaMultipleRequestDTO;
import com.mercadopagos.dto.request.DeudaRequestDTO;
import com.mercadopagos.dto.response.DeudaResponseDTO;
import com.mercadopagos.dto.response.MorosidadDTO;
import com.mercadopagos.dto.response.ResumenCobranzaDTO;
import com.mercadopagos.entity.Deuda;
import com.mercadopagos.entity.Puesto;
import com.mercadopagos.entity.Socio;
import com.mercadopagos.enums.EstadoDeuda;
import com.mercadopagos.enums.EstadoPuesto;
import com.mercadopagos.exception.ResourceNotFoundException;
import com.mercadopagos.mapper.DeudaMapper;
import com.mercadopagos.mapper.SocioMapper;
import com.mercadopagos.repository.DeudaRepository;
import com.mercadopagos.repository.PuestoRepository;
import com.mercadopagos.service.DeudaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class DeudaServiceImpl implements DeudaService {

    private final DeudaRepository deudaRepository;
    private final PuestoRepository puestoRepository;
    private final DeudaMapper deudaMapper;
    private final SocioMapper socioMapper;

    private void actualizarEstadosVencidos() {
        LocalDate hoy = LocalDate.now();
        List<Deuda> pendientes = deudaRepository.findByEstado(EstadoDeuda.PENDIENTE);
        for (Deuda deuda : pendientes) {
            if (deuda.getFechaVencimiento().isBefore(hoy)) {
                deuda.setEstado(EstadoDeuda.VENCIDO);
                deudaRepository.save(deuda);
            }
        }
    }

    @Override
    public DeudaResponseDTO crearDeuda(DeudaRequestDTO dto) {
        LocalDate hoy = LocalDate.now();

        if (dto.fechaEmision().isAfter(hoy)) {
            throw new IllegalArgumentException("La fecha de emisión no puede ser posterior a la fecha actual.");
        }

        if (!dto.fechaVencimiento().isAfter(dto.fechaEmision())) {
            throw new IllegalArgumentException("La fecha de vencimiento debe ser posterior a la fecha de emisión.");
        }

        Puesto puesto = puestoRepository.findById(dto.puestoId())
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + dto.puestoId()));

        if (puesto.getEstado() != EstadoPuesto.OCUPADO || puesto.getSocio() == null) {
            throw new IllegalStateException("El puesto debe estar ocupado para registrar una deuda.");
        }

        Deuda deuda = new Deuda();
        deuda.setPuesto(puesto);
        deuda.setSocio(puesto.getSocio());
        deuda.setConcepto(dto.concepto());
        deuda.setMonto(dto.monto());
        deuda.setFechaEmision(dto.fechaEmision());
        deuda.setFechaVencimiento(dto.fechaVencimiento());
        deuda.setDescripcion(dto.descripcion());

        deuda = deudaRepository.save(deuda);
        return deudaMapper.toDTO(deuda);
    }

    @Override
    public List<DeudaResponseDTO> crearDeudasMultiple(DeudaMultipleRequestDTO dto) {
        List<DeudaResponseDTO> resultado = new ArrayList<>();
        for (DeudaRequestDTO deudaDTO : dto.deudas()) {
            resultado.add(crearDeuda(deudaDTO));
        }
        return resultado;
    }

    @Override
    public List<DeudaResponseDTO> listarPendientes() {
        actualizarEstadosVencidos();
        List<Deuda> deudas = deudaRepository.findByEstado(EstadoDeuda.PENDIENTE);
        return deudaMapper.toDTOList(deudas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeudaResponseDTO> listarPagadas() {
        List<Deuda> deudas = deudaRepository.findByEstado(EstadoDeuda.PAGADO);
        return deudaMapper.toDTOList(deudas);
    }

    @Override
    public List<DeudaResponseDTO> listarVencidas() {
        actualizarEstadosVencidos();
        List<Deuda> deudas = deudaRepository.findByEstado(EstadoDeuda.VENCIDO);
        return deudaMapper.toDTOList(deudas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeudaResponseDTO> listarPorPuesto(Long puestoId) {
        Puesto puesto = puestoRepository.findById(puestoId)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + puestoId));

        List<Deuda> deudas = deudaRepository.findByPuesto(puesto);
        return deudaMapper.toDTOList(deudas);
    }

    @Override
    public List<DeudaResponseDTO> listarSinPuesto() {
        actualizarEstadosVencidos();
        List<EstadoDeuda> estados = new ArrayList<>();
        estados.add(EstadoDeuda.PENDIENTE);
        estados.add(EstadoDeuda.VENCIDO);
        List<Deuda> deudas = deudaRepository.findDeudasSinPuesto(estados);
        return deudaMapper.toDTOList(deudas);
    }

    @Override
    public List<MorosidadDTO> reporteMorosidad() {
        actualizarEstadosVencidos();

        List<Deuda> deudasVencidas = deudaRepository.findByEstado(EstadoDeuda.VENCIDO);

        Map<String, List<Deuda>> grupos = new LinkedHashMap<>();
        for (Deuda deuda : deudasVencidas) {
            String clave = deuda.getPuesto().getId() + "-" + deuda.getSocio().getId();
            if (!grupos.containsKey(clave)) {
                grupos.put(clave, new ArrayList<>());
            }
            grupos.get(clave).add(deuda);
        }

        List<MorosidadDTO> resultado = new ArrayList<>();
        for (Map.Entry<String, List<Deuda>> entry : grupos.entrySet()) {
            List<Deuda> deudas = entry.getValue();
            Deuda primera = deudas.get(0);

            BigDecimal totalMora = BigDecimal.ZERO;
            for (Deuda deuda : deudas) {
                totalMora = totalMora.add(deuda.getMonto());
            }

            resultado.add(new MorosidadDTO(
                    primera.getPuesto().getId(),
                    primera.getPuesto().getCodigo(),
                    primera.getSocio().getId(),
                    primera.getSocio().getNombres() + " " + primera.getSocio().getApellidos(),
                    deudaMapper.toDTOList(deudas),
                    totalMora
            ));
        }

        return resultado;
    }

    @Override
    public ResumenCobranzaDTO obtenerResumenCobranzas() {
        actualizarEstadosVencidos();

        List<EstadoDeuda> estadosActivos = new ArrayList<>();
        estadosActivos.add(EstadoDeuda.PENDIENTE);
        estadosActivos.add(EstadoDeuda.VENCIDO);

        List<Deuda> deudasActivas = deudaRepository.findByEstadoIn(estadosActivos);

        BigDecimal totalPendienteGeneral = BigDecimal.ZERO;
        Set<Long> puestosConDeuda = new HashSet<>();
        for (Deuda deuda : deudasActivas) {
            totalPendienteGeneral = totalPendienteGeneral.add(deuda.getMonto());
            puestosConDeuda.add(deuda.getPuesto().getId());
        }

        long totalDeudasPendientes = deudaRepository.countByEstado(EstadoDeuda.PENDIENTE);

        List<Socio> deudoresSinPuesto = deudaRepository.findDeudoresSinPuesto(estadosActivos);

        return new ResumenCobranzaDTO(
                totalPendienteGeneral,
                totalDeudasPendientes,
                (long) puestosConDeuda.size(),
                (long) deudoresSinPuesto.size(),
                socioMapper.toDTOList(deudoresSinPuesto)
        );
    }
}