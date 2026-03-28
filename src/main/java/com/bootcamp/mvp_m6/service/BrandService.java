package com.bootcamp.mvp_m6.service;

import com.bootcamp.mvp_m6.exceptions.ResourceAlreadyExistsException;
import com.bootcamp.mvp_m6.model.Brand;
import com.bootcamp.mvp_m6.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para las marcas
 */
@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    /**
     * Crea una nueva marca
     * @param brand Marca a crear
     * @return Marca creada
     * @throws ResourceAlreadyExistsException Cuando el nombre ya existe
     */
    @Transactional
    public Brand create(Brand brand) {
        if (brandRepository.existsByName(brand.getName())) {
            throw new ResourceAlreadyExistsException("Brand", "name", brand.getName());
        }

        return brandRepository.save(brand);
    }

    /**
     * Busca o crea una marca a partir del nombre
     * @param brandName Nombre de la marca
     * @return Marca existente o una nueva marca creada
     */
    @Transactional
    public Brand findOrCreate(String brandName) {
        return brandRepository.findByName(brandName).orElseGet(
                () -> brandRepository.save(Brand.builder().name(brandName).build())
        );
    }

    /**
     * Busca todas las marcas
     * @return Lista con todas las marcas
     */
    @Transactional(readOnly = true)
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    /**
     * Obtiene una referencia de la marca a partir de la id
     * @param id Id de la marca
     * @return Marca referenciada
     */
    @Transactional(readOnly = true)
    public Brand getReferenceById(Long id) {
        return brandRepository.getReferenceById(id);
    }

    /**
     * Verifica si existe una marca por su id
     * @param brandId Id de la marca
     * @return Verdadero enc aso de que exista, falso en caso contrario
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long brandId) {
        return brandRepository.existsById(brandId);
    }
}
