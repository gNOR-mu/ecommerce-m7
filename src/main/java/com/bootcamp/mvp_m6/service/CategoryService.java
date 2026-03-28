package com.bootcamp.mvp_m6.service;

import com.bootcamp.mvp_m6.exceptions.InvalidOperationException;
import com.bootcamp.mvp_m6.exceptions.ResourceAlreadyExistsException;
import com.bootcamp.mvp_m6.model.Category;
import com.bootcamp.mvp_m6.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para las categorías
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Crea una nueva categoría
     * @param category Categoría a crear
     * @return Categoría creada
     * @throws ResourceAlreadyExistsException Cuando el nombre de la categoría ya existe
     */
    @Transactional
    public Category create(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new ResourceAlreadyExistsException("Category", "name", category.getName());
        }
        return categoryRepository.save(category);
    }

    /**
     * Busca todas las categorías
     * @return Lista con todas las categorías
     */
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * Obtiene la referencia de una categoría
     * @param id Id de la categoría
     * @return Referencia de la categoría
     */
    @Transactional(readOnly = true)
    public Category getReferenceById(Long id) {
        return categoryRepository.getReferenceById(id);
    }

    /**
     * Verifica si existe una categoría por su id
     * @param categoryId Id de la categoría
     * @return Verdadero en caso de que la id exista, falso en caso contrario
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    /**
     * Busca o crea una categoría a partir del nombre
     * @param categoryName Nombre de la categoría
     * @return Categoría existente o categoría creada
     */
    @Transactional
    public Category findOrCreate(String categoryName) {
        return categoryRepository.findByName(categoryName).orElseGet(
                () -> categoryRepository.save(Category.builder().name(categoryName).build())
        );
    }
}
