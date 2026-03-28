package com.bootcamp.mvp_m6.repository;

import com.bootcamp.mvp_m6.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operar sobre User en la base de datos
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su email
     * @param email Email del usuario
     * @return Usuario coincidente
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica si existe un usuario por su email
     * @param email Email del usuario
     * @return Verdadero si el email existe, falso en caso contrario
     */
    boolean existsByEmail(String email);
}
