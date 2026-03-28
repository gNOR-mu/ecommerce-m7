package com.bootcamp.mvp_m6.repository;

import com.bootcamp.mvp_m6.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operar sobre Brand en la base de datos
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    /**
     * Verifica si un nombre existe
     * @param name Nombre a verificar
     * @return Verdadero si el nombre existe, falso en caso contrario
     */
    boolean existsByName(String name);

    /**
     * Busca una marca por el nombre
     * @param name Nombre a buscar
     * @return Marca con el nombre coincidente
     */
    Optional<Brand> findByName(String name);
}
