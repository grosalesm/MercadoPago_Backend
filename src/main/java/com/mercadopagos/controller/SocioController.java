package com.mercadopagos.controller;

import com.mercadopagos.dto.request.SocioRequestDTO;
import com.mercadopagos.dto.response.PuestoResponseDTO;
import com.mercadopagos.dto.response.SocioResponseDTO;
import com.mercadopagos.service.SocioService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/socios")
@RequiredArgsConstructor
public class SocioController {

    private final SocioService socioService;

    @GetMapping
    public ResponseEntity<List<SocioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(socioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(socioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<SocioResponseDTO> crear(@Valid @RequestBody SocioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(socioService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocioResponseDTO> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody SocioRequestDTO dto) {
        return ResponseEntity.ok(socioService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/bloquear")
    public ResponseEntity<SocioResponseDTO> bloquear(@PathVariable Long id,
                                                      @RequestBody(required = false) Map<String, String> body) {
        String observacion = (body != null) ? body.get("observacion") : null;
        return ResponseEntity.ok(socioService.bloquear(id, observacion));
    }

    @PatchMapping("/{id}/desbloquear")
    public ResponseEntity<SocioResponseDTO> desbloquear(@PathVariable Long id) {
        return ResponseEntity.ok(socioService.desbloquear(id));
    }

    @GetMapping("/{id}/puestos")
    public ResponseEntity<List<PuestoResponseDTO>> listarPuestosDeSocio(@PathVariable Long id) {
        return ResponseEntity.ok(socioService.listarPuestosDeSocio(id));
    }
}