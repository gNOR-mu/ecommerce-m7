package com.bootcamp.mvp_m6.mapper;

import com.bootcamp.mvp_m6.dto.cart.CartItemDTO;
import com.bootcamp.mvp_m6.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para {@link CartItem}
 */
@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "product.id", target = "id")
    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.price", target = "unitPrice")
    CartItemDTO toItemDto(CartItem item);
}