package com.mercadopagos.controller;

import com.mercadopagos.dto.request.PagoRequestDTO;
import com.mercadopagos.dto.response.PagoResponseDTO;
import com.mercadopagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    // Pagar una deuda específica (pago completo)
    @PostMapping("/individual")
    public ResponseEntity<PagoResponseDTO> pagarDeudaIndividual(@Valid @RequestBody PagoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.pagarDeudaIndividual(dto));
    }

    // Pagar la totalidad de deudas pendientes de un puesto (boleta completa)
    @PostMapping("/puesto/{puestoId}/total")
    public ResponseEntity<List<PagoResponseDTO>> pagarTotalPuesto(@PathVariable Long puestoId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.pagarTotalPuesto(puestoId));
    }
}