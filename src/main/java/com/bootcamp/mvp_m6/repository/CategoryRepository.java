package com.bootcamp.mvp_m6.repository;

import com.bootcamp.mvp_m6.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operar sobre Category en la base de datos
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Verifica si una categoría existe por nombre
     * @param name Nombre de la categoría
     * @return Categoría coincidente
     */
    boolean existsByName(String name);

    /**
     * Busca una categoría por el nombre
     * @param name Nombre de la categoría
     * @return Categoría coincidente
     */
    Optional<Category> findByName(String name);

}
