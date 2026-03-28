package com.bootcamp.mvp_m6.repository;

import com.bootcamp.mvp_m6.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operar sobre OrderItem en la base de datos
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
