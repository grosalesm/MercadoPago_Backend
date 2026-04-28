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
public class DeudaServiceImpl implements DeudaService {

    private final DeudaRepository deudaRepository;
    private final PuestoRepository puestoRepository;
    private final DeudaMapper deudaMapper;
    private final SocioMapper socioMapper;

    public DeudaServiceImpl(DeudaRepository deudaRepository,
                            PuestoRepository puestoRepository,
                            DeudaMapper deudaMapper,
                            SocioMapper socioMapper) {
        this.deudaRepository = deudaRepository;
        this.puestoRepository = puestoRepository;
        this.deudaMapper = deudaMapper;
        this.socioMapper = socioMapper;
    }

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

        if (dto.getFechaEmision().isAfter(hoy)) {
            throw new IllegalArgumentException("La fecha de emisión no puede ser posterior a la fecha actual.");
        }

        if (!dto.getFechaVencimiento().isAfter(dto.getFechaEmision())) {
            throw new IllegalArgumentException("La fecha de vencimiento debe ser posterior a la fecha de emisión.");
        }

        Puesto puesto = puestoRepository.findById(dto.getPuestoId())
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + dto.getPuestoId()));

        if (puesto.getEstado() != EstadoPuesto.OCUPADO || puesto.getSocio() == null) {
            throw new IllegalStateException("El puesto debe estar ocupado para registrar una deuda.");
        }

        Deuda deuda = new Deuda();
        deuda.setPuesto(puesto);
        deuda.setSocio(puesto.getSocio());
        deuda.setConcepto(dto.getConcepto());
        deuda.setMonto(dto.getMonto());
        deuda.setFechaEmision(dto.getFechaEmision());
        deuda.setFechaVencimiento(dto.getFechaVencimiento());
        deuda.setDescripcion(dto.getDescripcion());

        deuda = deudaRepository.save(deuda);
        return deudaMapper.toDTO(deuda);
    }

    @Override
    public List<DeudaResponseDTO> crearDeudasMultiple(DeudaMultipleRequestDTO dto) {
        List<DeudaResponseDTO> resultado = new ArrayList<>();
        for (DeudaRequestDTO deudaDTO : dto.getDeudas()) {
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

            MorosidadDTO mora = new MorosidadDTO();
            mora.setPuestoId(primera.getPuesto().getId());
            mora.setCodigoPuesto(primera.getPuesto().getCodigo());
            mora.setSocioId(primera.getSocio().getId());
            mora.setSocioNombreCompleto(primera.getSocio().getNombres() + " " + primera.getSocio().getApellidos());
            mora.setDeudasVencidas(deudaMapper.toDTOList(deudas));
            mora.setTotalMora(totalMora);

            resultado.add(mora);
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

        ResumenCobranzaDTO resumen = new ResumenCobranzaDTO();
        resumen.setTotalPendienteGeneral(totalPendienteGeneral);
        resumen.setTotalDeudasPendientes(totalDeudasPendientes);
        resumen.setTotalPuestosConDeuda((long) puestosConDeuda.size());
        resumen.setTotalDeudoresSinPuesto((long) deudoresSinPuesto.size());
        resumen.setDeudoresSinPuesto(socioMapper.toDTOList(deudoresSinPuesto));

        return resumen;
    }
}