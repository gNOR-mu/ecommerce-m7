package com.bootcamp.mvp_m6.dto.cart;

import java.math.BigDecimal;

/**
 * DTO para mostrar un producto en el carro
 *
 * @param id        Identificación del producto
 * @param name      Nombre del producto
 * @param unitPrice Precio unitario
 * @param quantity  Cantidad
 * @param subTotal  Subtotal
 * @author Gabriel Norambuena
 * @version 1.1
 * @apiNote Adaptado del mvp m4
 */
public record CartItemDTO(
        Long id,
        String name,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subTotal

) {
}
