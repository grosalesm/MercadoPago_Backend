package com.mercadopagos.repository;

import com.mercadopagos.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByDeudaId(Long deudaId);

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.fechaPago = :fecha")
    Double sumMontoByFechaPago(@Param("fecha") LocalDate fecha);

}