package com.bootcamp.mvp_m6.dto.cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para añadir productos a un carrito
 *
 * @param productId Id del producto
 * @param quantity  Cantidad a añadir
 */
public record AddToCartDTO(
        @NotNull
        Long productId,

        @Min(value = 1, message = "Mínimo 1 producto")
        @Max(value = 5, message = "No puedes agregar más de 5")
        Integer quantity
) {
}