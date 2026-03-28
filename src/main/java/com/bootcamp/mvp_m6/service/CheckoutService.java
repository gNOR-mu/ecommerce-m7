package com.bootcamp.mvp_m6.service;

import com.bootcamp.mvp_m6.dto.cart.CartItemDTO;
import com.bootcamp.mvp_m6.dto.cart.CartSummaryDTO;
import com.bootcamp.mvp_m6.exceptions.InvalidOperationException;
import com.bootcamp.mvp_m6.model.Cart;
import com.bootcamp.mvp_m6.model.CartItem;
import com.bootcamp.mvp_m6.model.Product;
import com.bootcamp.mvp_m6.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para el checkout
 */
@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;


    /**
     * Procesa la compra
     *
     * @param user Usuario al que le pertenece el carro
     * @throws InvalidOperationException Cuando el total base es inferior o igual a 0
     * @throws InvalidOperationException Cuando se intenta aplicar descuentos negativos
     */
    @Transactional
    public void checkout(User user) {
        Cart cart = cartService.getCart(user);

        // Valido y reduzco el stock
        productService.validateAndReduceStock(cart);

        //Creo las órdenes y orderItems
        orderService.createFromCart(user);

        // Limpieza final del carrito
        cartService.clearCart(user);
    }
}
