package com.bootcamp.mvp_m6.service;

import com.bootcamp.mvp_m6.dto.cart.CartPricing;
import com.bootcamp.mvp_m6.model.*;
import com.bootcamp.mvp_m6.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para las órdenes
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    /**
     * Crea una orden a partir del carrito de un usuario
     * @param user Usuario relacionado con el carrito
     * @return Orden creada
     */
    @Transactional
    public Order createFromCart(User user) {
        Cart cart = cartService.getCart(user);
        CartPricing cartPricing = cartService.calculatePricing(cart);

        Order neworder = Order.builder()
                .user(user)
                .total(cartPricing.totalFinal())
                .subtotal(cartPricing.subtotal())
                .totalDiscounts(cartPricing.totalDiscounts())
                .discountConditions(cartPricing.discountConditions())
                .build();

        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .unitPrice(item.getProduct().getPrice())
                    .subTotal(item.getSubTotal())
                    .quantity(item.getQuantity())
                    .product(item.getProduct())
                    .build();

            neworder.addOrderItem(orderItem);
        }
        return orderRepository.save(neworder);
    }
}
