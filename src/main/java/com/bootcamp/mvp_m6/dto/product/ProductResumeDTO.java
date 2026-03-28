package com.bootcamp.mvp_m6.dto.product;


import java.math.BigDecimal;


/**
 * DTO con información simplificada de un producto
 *
 * @param id Id del producto
 * @param name Nombre del producto
 * @param shortDescription Descripción corta del producto
 * @param urlImage Link de la imagen del producto
 * @param price Precio del producto
 */
public record ProductResumeDTO(
        Long id,
        String name,
        String shortDescription,
        String urlImage,
        BigDecimal price) {
}