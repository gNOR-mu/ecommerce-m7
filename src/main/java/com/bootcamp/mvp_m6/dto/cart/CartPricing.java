package com.bootcamp.mvp_m6.dto.cart;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para el precio del carrito
 *
 * @param subtotal           Subtotal
 * @param totalDiscounts     Descuentos totales
 * @param totalFinal         Total final
 * @param discountConditions Lista con las condiciones de descuento aplicadas
 */
public record CartPricing(
        BigDecimal subtotal,
        BigDecimal totalDiscounts,
        BigDecimal totalFinal,
        List<String> discountConditions
) {
}
