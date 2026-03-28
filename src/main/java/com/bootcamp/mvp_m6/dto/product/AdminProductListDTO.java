package com.bootcamp.mvp_m6.dto.product;

import java.math.BigDecimal;

/**
 * DTO con información de los productos como administrador
 * @param id Id del producto
 * @param price Precio del producto
 * @param name Nombre del producto
 * @param categoryName Categoría del producto
 * @param brandName Marca del producto
 * @param stock Stock del producto
 */
public record AdminProductListDTO(
        Long id,
        BigDecimal price,
        String name,
        String categoryName,
        String brandName,
        Integer stock
) {
}
