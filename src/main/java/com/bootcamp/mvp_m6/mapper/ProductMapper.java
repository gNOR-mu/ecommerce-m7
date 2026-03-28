package com.bootcamp.mvp_m6.mapper;

import com.bootcamp.mvp_m6.dto.product.ProductFormDTO;
import com.bootcamp.mvp_m6.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

/**
 * Mapper para {@link Product}
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {


    @Mapping(source = "brandId", target = "brand.id")
    @Mapping(source = "brandId", target = "category.id")
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "cartItem", ignore = true)
    Product toEntity(ProductFormDTO dto);

    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "featureKeys", ignore = true)
    @Mapping(target = "featureValues", ignore = true)
    ProductFormDTO toDTO(Product product);

    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "cartItem", ignore = true)
    void updateEntityFromDTO(ProductFormDTO dto, @MappingTarget Product entity);
}
