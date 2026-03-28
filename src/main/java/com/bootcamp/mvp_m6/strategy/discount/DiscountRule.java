package com.bootcamp.mvp_m6.strategy.discount;

import com.bootcamp.mvp_m6.model.Cart;

import java.math.BigDecimal;

/**
 * Interfaz para definir las reglas de descuento que aplican a los productos.
 *
 * @author Gabriel Norambuena
 * @version 1.1
 * @apiNote Adaptado del mvp m4
 */
public interface DiscountRule {
    /**
     * Verifica si un carrito determinado es aplicable o no para los descuentos
     *
     * @param cart carrito a evaluar
     * @return verdadero si aplican los descuentos, falso en caso contrario
     */
    boolean isApplicable(Cart cart);

    /**
     * Descuento correspondiente a un carrito.
     *
     * @return Descuento
     */
    BigDecimal calculateDiscount(Cart cart);

    /**
     * Obtiene el nombre de la regla de descuento
     *
     * @return Nombre del descuento
     */
    String getName();

    /**
     * Obtiene la condición de descuento
     *
     * @return Condición de descuento
     */
    String getCondition();
}
