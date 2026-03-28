package com.bootcamp.mvp_m6.mapper;

import com.bootcamp.mvp_m6.dto.cart.CartPricing;
import com.bootcamp.mvp_m6.dto.cart.CartSummaryDTO;
import com.bootcamp.mvp_m6.model.Cart;
import org.mapstruct.Mapper;

/**
 * Mapper para {@link Cart}
 */
@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {

    CartSummaryDTO toSummaryDTO(Cart cart, CartPricing cartPricing);
}
