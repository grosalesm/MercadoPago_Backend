package com.mercadopagos.entity;

import com.mercadopagos.converter.TipoPuestoConverter;
import com.mercadopagos.enums.EstadoPuesto;
import com.mercadopagos.enums.TipoPuesto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "puestos")
@Getter
@Setter
@NoArgsConstructor
public class Puesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(length = 200)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPuesto estado;

    @Convert(converter = TipoPuestoConverter.class)
    @Column(length = 20)
    private TipoPuesto tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio_id")
    private Socio socio;

    @PrePersist
    public void prePersist() {
        if (estado == null) {
            estado = EstadoPuesto.LIBRE;
        }
    }
}