package com.mercadopagos.repository;

import com.mercadopagos.entity.Puesto;
import com.mercadopagos.enums.EstadoPuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuestoRepository extends JpaRepository<Puesto, Long> {

    List<Puesto> findByEstado(EstadoPuesto estado);

    long countByEstado(EstadoPuesto estado);

    List<Puesto> findBySocioId(Long socioId);

    long countBySocioId(Long socioId);

    boolean existsByCodigo(String codigo);
}