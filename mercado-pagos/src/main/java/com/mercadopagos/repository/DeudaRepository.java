package com.mercadopagos.repository;

import com.mercadopagos.entity.Deuda;
import com.mercadopagos.entity.Puesto;
import com.mercadopagos.entity.Socio;
import com.mercadopagos.enums.EstadoDeuda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeudaRepository extends JpaRepository<Deuda, Long> {

    List<Deuda> findByEstado(EstadoDeuda estado);

    List<Deuda> findByEstadoIn(List<EstadoDeuda> estados);

    List<Deuda> findByPuesto(Puesto puesto);

    List<Deuda> findByPuestoAndEstadoIn(Puesto puesto, List<EstadoDeuda> estados);

    long countByEstado(EstadoDeuda estado);

    long countByEstadoIn(List<EstadoDeuda> estados);

    // Socios con deuda activa (PENDIENTE o VENCIDO) que ya no están asignados a ningún puesto
    @Query("SELECT DISTINCT d.socio FROM Deuda d " +
           "WHERE d.estado IN :estados " +
           "AND d.socio NOT IN (SELECT p.socio FROM Puesto p WHERE p.socio IS NOT NULL)")
    List<Socio> findDeudoresSinPuesto(@Param("estados") List<EstadoDeuda> estados);
}