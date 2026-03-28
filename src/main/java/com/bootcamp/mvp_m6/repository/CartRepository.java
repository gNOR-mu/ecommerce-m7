package com.bootcamp.mvp_m6.repository;

import com.bootcamp.mvp_m6.model.Cart;
import com.bootcamp.mvp_m6.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para operar sobre Cart en la base de datos
 */
public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * Busca un carrito por el usuario
     * @param user Usuario relacionado al carrito
     * @return Carrito
     */
    Optional<Cart> findByUser(User user);
}
