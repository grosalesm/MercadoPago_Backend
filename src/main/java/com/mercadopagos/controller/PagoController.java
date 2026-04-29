package com.mercadopagos.controller;

import com.mercadopagos.dto.request.PagoRequestDTO;
import com.mercadopagos.dto.response.PagoResponseDTO;
import com.mercadopagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping("/individual")
    public ResponseEntity<PagoResponseDTO> pagarDeudaIndividual(@Valid @RequestBody PagoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.pagarDeudaIndividual(dto));
    }

    @PostMapping("/puesto/{puestoId}/total")
    public ResponseEntity<List<PagoResponseDTO>> pagarTotalPuesto(@PathVariable Long puestoId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.pagarTotalPuesto(puestoId));
    }

    @GetMapping("/total-hoy")
    public ResponseEntity<Map<String, Double>> getTotalHoy() {
        Double total = pagoService.obtenerTotalPagadoHoy();
        return ResponseEntity.ok(Map.of("total", total != null ? total : 0.0));
    }
}