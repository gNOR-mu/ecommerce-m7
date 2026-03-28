package com.bootcamp.mvp_m6.dto.cart;

import java.util.List;

/**
 * DTO para mostrar un resumen del carrito
 *
 * @param items       Lista de  {@link CartItemDTO}
 * @param cartPricing Precios del carrito {@link CartPricing}
 */
public record CartSummaryDTO(
        List<CartItemDTO> items,
        CartPricing cartPricing
) {
}
