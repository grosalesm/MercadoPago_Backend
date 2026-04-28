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

    // Registrar una deuda individual (puede ser retroactiva: fechaEmision <= hoy)
    @PostMapping
    public ResponseEntity<DeudaResponseDTO> crearDeuda(@Valid @RequestBody DeudaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deudaService.crearDeuda(dto));
    }

    // Registrar 2, 3 o n deudas en una sola solicitud
    @PostMapping("/multiple")
    public ResponseEntity<List<DeudaResponseDTO>> crearDeudasMultiple(@Valid @RequestBody DeudaMultipleRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deudaService.crearDeudasMultiple(dto));
    }

    // Listas por estado
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

    // Deudas históricas de un puesto (incluye deudas de socios anteriores)
    @GetMapping("/puesto/{puestoId}")
    public ResponseEntity<List<DeudaResponseDTO>> listarPorPuesto(@PathVariable Long puestoId) {
        return ResponseEntity.ok(deudaService.listarPorPuesto(puestoId));
    }

    // Reporte de morosidad: puestos con deudas vencidas sin pagar
    @GetMapping("/morosidad")
    public ResponseEntity<List<MorosidadDTO>> reporteMorosidad() {
        return ResponseEntity.ok(deudaService.reporteMorosidad());
    }

    // Tarjetas informativas de cobranzas
    @GetMapping("/resumen-cobranzas")
    public ResponseEntity<ResumenCobranzaDTO> obtenerResumenCobranzas() {
        return ResponseEntity.ok(deudaService.obtenerResumenCobranzas());
    }
}