package com.mercadopagos.entity;

import com.mercadopagos.enums.ConceptoDeuda;
import com.mercadopagos.enums.EstadoDeuda;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "deudas")
@Getter
@Setter
@NoArgsConstructor
public class Deuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación Puesto-Socio guardada en la deuda para preservar historial
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puesto_id", nullable = false)
    private Puesto puesto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio_id", nullable = false)
    private Socio socio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConceptoDeuda concepto;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoDeuda estado;

    @Column(length = 300)
    private String descripcion;

    @PrePersist
    public void prePersist() {
        if (estado == null) {
            estado = EstadoDeuda.PENDIENTE;
        }
    }
}