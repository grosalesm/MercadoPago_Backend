package com.mercadopagos.service.impl;

import com.mercadopagos.dto.request.SocioRequestDTO;
import com.mercadopagos.dto.response.PuestoResponseDTO;
import com.mercadopagos.dto.response.SocioResponseDTO;
import com.mercadopagos.entity.Puesto;
import com.mercadopagos.entity.Socio;
import com.mercadopagos.enums.EstadoPuesto;
import com.mercadopagos.exception.ResourceNotFoundException;
import com.mercadopagos.mapper.PuestoMapper;
import com.mercadopagos.mapper.SocioMapper;
import com.mercadopagos.repository.PuestoRepository;
import com.mercadopagos.repository.SocioRepository;
import com.mercadopagos.service.SocioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SocioServiceImpl implements SocioService {

    private final SocioRepository socioRepository;
    private final PuestoRepository puestoRepository;
    private final SocioMapper socioMapper;
    private final PuestoMapper puestoMapper;

    public SocioServiceImpl(SocioRepository socioRepository,
                            PuestoRepository puestoRepository,
                            SocioMapper socioMapper,
                            PuestoMapper puestoMapper) {
        this.socioRepository = socioRepository;
        this.puestoRepository = puestoRepository;
        this.socioMapper = socioMapper;
        this.puestoMapper = puestoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocioResponseDTO> listarTodos() {
        List<Socio> socios = socioRepository.findAll();
        return socioMapper.toDTOList(socios);
    }

    @Override
    @Transactional(readOnly = true)
    public SocioResponseDTO obtenerPorId(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con ID: " + id));
        return socioMapper.toDTO(socio);
    }

    @Override
    public SocioResponseDTO crear(SocioRequestDTO dto) {
        if (socioRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("Ya existe un socio con el DNI: " + dto.getDni());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && socioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un socio con el email: " + dto.getEmail());
        }

        Socio socio = new Socio();
        socio.setNombres(dto.getNombres());
        socio.setApellidos(dto.getApellidos());
        socio.setDni(dto.getDni());
        socio.setTelefono(dto.getTelefono());
        socio.setEmail(dto.getEmail());

        socio = socioRepository.save(socio);
        return socioMapper.toDTO(socio);
    }

    @Override
    public SocioResponseDTO actualizar(Long id, SocioRequestDTO dto) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con ID: " + id));

        if (!socio.getDni().equals(dto.getDni()) && socioRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("Ya existe un socio con el DNI: " + dto.getDni());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && socioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new IllegalArgumentException("Ya existe un socio con el email: " + dto.getEmail());
        }

        socio.setNombres(dto.getNombres());
        socio.setApellidos(dto.getApellidos());
        socio.setDni(dto.getDni());
        socio.setTelefono(dto.getTelefono());
        socio.setEmail(dto.getEmail());

        socio = socioRepository.save(socio);
        return socioMapper.toDTO(socio);
    }

    @Override
    public void eliminar(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con ID: " + id));
        socio.setActivo(false);
        socioRepository.save(socio);
    }

    @Override
    public SocioResponseDTO bloquear(Long id, String observacion) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con ID: " + id));

        if (!socio.getActivo()) {
            throw new IllegalStateException("El socio ya se encuentra bloqueado.");
        }

        if (puestoRepository.existsBySocioIdAndEstado(id, EstadoPuesto.OCUPADO)) {
            throw new IllegalStateException("No se puede bloquear un socio que tiene un puesto asignado. Libere el puesto primero.");
        }

        socio.setActivo(false);
        socio.setObservacion(observacion != null ? observacion.trim() : null);
        socio = socioRepository.save(socio);
        return socioMapper.toDTO(socio);
    }

    @Override
    public SocioResponseDTO desbloquear(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con ID: " + id));

        if (socio.getActivo()) {
            throw new IllegalStateException("El socio ya se encuentra activo.");
        }

        socio.setActivo(true);
        socio.setObservacion(null);
        socio = socioRepository.save(socio);
        return socioMapper.toDTO(socio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PuestoResponseDTO> listarPuestosDeSocio(Long socioId) {
        if (!socioRepository.existsById(socioId)) {
            throw new ResourceNotFoundException("Socio no encontrado con ID: " + socioId);
        }

        List<Puesto> puestos = puestoRepository.findBySocioId(socioId);
        return puestoMapper.toDTOList(puestos);
    }
}