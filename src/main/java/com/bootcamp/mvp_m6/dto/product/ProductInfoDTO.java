package com.bootcamp.mvp_m6.dto.product;


import java.math.BigDecimal;
import java.util.Map;


/**
 * DTO con la información de un producto
 *
 * @param id Id del producto
 * @param price Precio del producto
 * @param features Características del producto
 * @param name Nombre del producto
 * @param urlImage Link de la imagen del producto
 * @param description Descripción del producto
 * @param shortDescription Descripción corta del producto
 */
public record ProductInfoDTO(
        Long id,
        BigDecimal price,
        Map<String, Object> features,
        String name,
        String urlImage,
        String description,
        String shortDescription
) {
}
