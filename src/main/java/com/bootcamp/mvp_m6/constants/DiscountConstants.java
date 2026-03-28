package com.bootcamp.mvp_m6.constants;

import java.math.BigDecimal;

/**
 * Constantes para las reglas de descuento
 * @apiNote Todos los descuentos se miden en porcentaje
 */
public class DiscountConstants {
    private DiscountConstants(){}

    /**
     * Umbral sobre el que se aplica la regla de cantidad de productos.
     * Siempre es {@value }
     */
    public static final int QUANTITY_THRESHOLD = 10;

    /**
     * Descuento que aplica a la regla de cantidad de productos
     */
    public static final BigDecimal QUANTITY_DISCOUNT_AMOUNT = new BigDecimal("5");


    /**
     * Descuento que se aplica al tener {@value} categorías distintas.
     */
    public static final int CATEGORIES_THRESHOLD = 2;

    /**
     * Descuento que aplica a la regla de cantidad de categorías
     */
    public static final BigDecimal CATEGORIES_DISCOUNT_AMOUNT = new BigDecimal("20");

    /**
     * Descuento que se aplica al tener la cantidad indicada como valor total del carrito pre descuentos.
     */
    public static final BigDecimal TOTAL_AMOUNT_THRESHOLD = new BigDecimal("99990");

    /**
     * Descuento que aplica a la regla de monto total
     */
    public static final BigDecimal TOTAL_COST_AMOUNT = new BigDecimal("5");

}
