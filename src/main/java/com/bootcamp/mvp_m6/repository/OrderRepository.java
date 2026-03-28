package com.bootcamp.mvp_m6.repository;

import com.bootcamp.mvp_m6.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operar sobre Order en la base de datos
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Verifica si la tabla Order tiene filas
     * @return Verdadero si tiene filas, falso en caso contrario
     */
    @Query("SELECT EXISTS (SELECT 1 FROM Order)")
    boolean hasData();
}
