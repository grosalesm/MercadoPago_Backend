package com.mercadopagos.service.impl;

import com.mercadopagos.dto.request.AsignarSocioRequestDTO;
import com.mercadopagos.dto.request.PuestoRequestDTO;
import com.mercadopagos.dto.response.PuestoEstadisticasDTO;
import com.mercadopagos.dto.response.PuestoResponseDTO;
import com.mercadopagos.entity.Puesto;
import com.mercadopagos.entity.Socio;
import com.mercadopagos.enums.EstadoPuesto;
import com.mercadopagos.exception.ResourceNotFoundException;
import com.mercadopagos.mapper.PuestoMapper;
import com.mercadopagos.repository.PuestoRepository;
import com.mercadopagos.repository.SocioRepository;
import com.mercadopagos.service.PuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PuestoServiceImpl implements PuestoService {

    private final PuestoRepository puestoRepository;
    private final SocioRepository socioRepository;
    private final PuestoMapper puestoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PuestoResponseDTO> listarTodos() {
        List<Puesto> puestos = puestoRepository.findAll();
        return puestoMapper.toDTOList(puestos);
    }

    @Override
    @Transactional(readOnly = true)
    public PuestoResponseDTO obtenerPorId(Long id) {
        Puesto puesto = puestoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + id));
        return puestoMapper.toDTO(puesto);
    }

    @Override
    public PuestoResponseDTO crear(PuestoRequestDTO dto) {
        if (puestoRepository.existsByCodigo(dto.codigo())) {
            throw new IllegalArgumentException("Ya existe un puesto con el código: " + dto.codigo());
        }

        Puesto puesto = new Puesto();
        puesto.setCodigo(dto.codigo());
        puesto.setDescripcion(dto.descripcion());
        puesto.setTipo(dto.tipo());

        puesto = puestoRepository.save(puesto);
        return puestoMapper.toDTO(puesto);
    }

    @Override
    public PuestoResponseDTO actualizar(Long id, PuestoRequestDTO dto) {
        Puesto puesto = puestoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + id));

        if (!puesto.getCodigo().equals(dto.codigo()) && puestoRepository.existsByCodigo(dto.codigo())) {
            throw new IllegalArgumentException("Ya existe un puesto con el código: " + dto.codigo());
        }

        puesto.setCodigo(dto.codigo());
        puesto.setDescripcion(dto.descripcion());
        puesto.setTipo(dto.tipo());

        puesto = puestoRepository.save(puesto);
        return puestoMapper.toDTO(puesto);
    }

    @Override
    public PuestoResponseDTO inhabilitar(Long id) {
        Puesto puesto = puestoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + id));

        if (puesto.getEstado() == EstadoPuesto.OCUPADO) {
            throw new IllegalStateException("No se puede inhabilitar un puesto ocupado. Primero libere el puesto.");
        }

        if (puesto.getEstado() == EstadoPuesto.INHABILITADO) {
            throw new IllegalStateException("El puesto ya se encuentra inhabilitado.");
        }

        puesto.setEstado(EstadoPuesto.INHABILITADO);
        puesto = puestoRepository.save(puesto);
        return puestoMapper.toDTO(puesto);
    }

    @Override
    public PuestoResponseDTO habilitar(Long id) {
        Puesto puesto = puestoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + id));

        if (puesto.getEstado() != EstadoPuesto.INHABILITADO) {
            throw new IllegalStateException("El puesto no se encuentra inhabilitado.");
        }

        puesto.setEstado(EstadoPuesto.LIBRE);
        puesto = puestoRepository.save(puesto);
        return puestoMapper.toDTO(puesto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PuestoResponseDTO> listarOcupados() {
        List<Puesto> puestos = puestoRepository.findByEstado(EstadoPuesto.OCUPADO);
        return puestoMapper.toDTOList(puestos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PuestoResponseDTO> listarLibres() {
        List<Puesto> puestos = puestoRepository.findByEstado(EstadoPuesto.LIBRE);
        return puestoMapper.toDTOList(puestos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PuestoResponseDTO> listarInhabilitados() {
        List<Puesto> puestos = puestoRepository.findByEstado(EstadoPuesto.INHABILITADO);
        return puestoMapper.toDTOList(puestos);
    }

    @Override
    @Transactional(readOnly = true)
    public PuestoEstadisticasDTO obtenerEstadisticas() {
        long ocupados = puestoRepository.countByEstado(EstadoPuesto.OCUPADO);
        long libres = puestoRepository.countByEstado(EstadoPuesto.LIBRE);
        long inhabilitados = puestoRepository.countByEstado(EstadoPuesto.INHABILITADO);

        return new PuestoEstadisticasDTO(ocupados + libres + inhabilitados, ocupados, libres, inhabilitados);
    }

    @Override
    public PuestoResponseDTO asignarSocio(Long puestoId, AsignarSocioRequestDTO dto) {
        Puesto puesto = puestoRepository.findById(puestoId)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + puestoId));

        if (puesto.getEstado() == EstadoPuesto.OCUPADO) {
            throw new IllegalStateException("El puesto ya está ocupado. Debe liberarlo antes de asignar otro socio.");
        }

        if (puesto.getEstado() == EstadoPuesto.INHABILITADO) {
            throw new IllegalStateException("No se puede asignar un socio a un puesto inhabilitado.");
        }

        Socio socio = socioRepository.findById(dto.socioId())
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con ID: " + dto.socioId()));

        if (!socio.getActivo()) {
            throw new IllegalStateException("No se puede asignar un puesto a un socio bloqueado.");
        }

        puesto.setSocio(socio);
        puesto.setEstado(EstadoPuesto.OCUPADO);
        puesto = puestoRepository.save(puesto);

        return puestoMapper.toDTO(puesto);
    }

    @Override
    public PuestoResponseDTO liberarPuesto(Long puestoId) {
        Puesto puesto = puestoRepository.findById(puestoId)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + puestoId));

        if (puesto.getEstado() == EstadoPuesto.LIBRE) {
            throw new IllegalStateException("El puesto ya se encuentra libre.");
        }

        puesto.setSocio(null);
        puesto.setEstado(EstadoPuesto.LIBRE);
        puesto = puestoRepository.save(puesto);

        return puestoMapper.toDTO(puesto);
    }
}