package com.mercadopagos.controller;

import com.mercadopagos.dto.request.DeudaMultipleRequestDTO;
import com.mercadopagos.dto.request.DeudaRequestDTO;
import com.mercadopagos.dto.response.DeudaResponseDTO;
import com.mercadopagos.dto.response.MorosidadDTO;
import com.mercadopagos.dto.response.ResumenCobranzaDTO;
import com.mercadopagos.service.DeudaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deudas")
public class DeudaController {

    private final DeudaService deudaService;

    public DeudaController(DeudaService deudaService) {
        this.deudaService = deudaService;
    }

    @PostMapping
    public ResponseEntity<DeudaResponseDTO> crearDeuda(@Valid @RequestBody DeudaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deudaService.crearDeuda(dto));
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<DeudaResponseDTO>> crearDeudasMultiple(@Valid @RequestBody DeudaMultipleRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deudaService.crearDeudasMultiple(dto));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<DeudaResponseDTO>> listarPendientes() {
        return ResponseEntity.ok(deudaService.listarPendientes());
    }

    @GetMapping("/pagadas")
    public ResponseEntity<List<DeudaResponseDTO>> listarPagadas() {
        return ResponseEntity.ok(deudaService.listarPagadas());
    }

    @GetMapping("/vencidas")
    public ResponseEntity<List<DeudaResponseDTO>> listarVencidas() {
        return ResponseEntity.ok(deudaService.listarVencidas());
    }

    @GetMapping("/puesto/{puestoId}")
    public ResponseEntity<List<DeudaResponseDTO>> listarPorPuesto(@PathVariable Long puestoId) {
        return ResponseEntity.ok(deudaService.listarPorPuesto(puestoId));
    }

    @GetMapping("/sin-puesto")
    public ResponseEntity<List<DeudaResponseDTO>> listarSinPuesto() {
        return ResponseEntity.ok(deudaService.listarSinPuesto());
    }

    @GetMapping("/morosidad")
    public ResponseEntity<List<MorosidadDTO>> reporteMorosidad() {
        return ResponseEntity.ok(deudaService.reporteMorosidad());
    }

    @GetMapping("/resumen-cobranzas")
    public ResponseEntity<ResumenCobranzaDTO> obtenerResumenCobranzas() {
        return ResponseEntity.ok(deudaService.obtenerResumenCobranzas());
    }
}