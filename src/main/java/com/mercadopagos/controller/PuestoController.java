package com.mercadopagos.controller;

import com.mercadopagos.dto.request.AsignarSocioRequestDTO;
import com.mercadopagos.dto.request.PuestoRequestDTO;
import com.mercadopagos.dto.response.PuestoEstadisticasDTO;
import com.mercadopagos.dto.response.PuestoResponseDTO;
import com.mercadopagos.service.PuestoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puestos")
public class PuestoController {

    private final PuestoService puestoService;

    public PuestoController(PuestoService puestoService) {
        this.puestoService = puestoService;
    }

    @GetMapping
    public ResponseEntity<List<PuestoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(puestoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuestoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(puestoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<PuestoResponseDTO> crear(@Valid @RequestBody PuestoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(puestoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PuestoResponseDTO> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody PuestoRequestDTO dto) {
        return ResponseEntity.ok(puestoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        puestoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Tarjetas informativas
    @GetMapping("/estadisticas")
    public ResponseEntity<PuestoEstadisticasDTO> obtenerEstadisticas() {
        return ResponseEntity.ok(puestoService.obtenerEstadisticas());
    }

    @GetMapping("/ocupados")
    public ResponseEntity<List<PuestoResponseDTO>> listarOcupados() {
        return ResponseEntity.ok(puestoService.listarOcupados());
    }

    @GetMapping("/libres")
    public ResponseEntity<List<PuestoResponseDTO>> listarLibres() {
        return ResponseEntity.ok(puestoService.listarLibres());
    }

    // Asignar un socio a un puesto libre
    @PostMapping("/{id}/asignar-socio")
    public ResponseEntity<PuestoResponseDTO> asignarSocio(@PathVariable Long id,
                                                           @Valid @RequestBody AsignarSocioRequestDTO dto) {
        return ResponseEntity.ok(puestoService.asignarSocio(id, dto));
    }

    // Liberar un puesto (queda libre, deuda histórica se preserva)
    @PostMapping("/{id}/liberar")
    public ResponseEntity<PuestoResponseDTO> liberarPuesto(@PathVariable Long id) {
        return ResponseEntity.ok(puestoService.liberarPuesto(id));
    }
}