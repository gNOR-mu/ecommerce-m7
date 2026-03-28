package com.bootcamp.mvp_m6.strategy.discount.rules;

import com.bootcamp.mvp_m6.constants.DiscountConstants;
import com.bootcamp.mvp_m6.model.Cart;
import com.bootcamp.mvp_m6.strategy.discount.DiscountRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Regla para los descuentos por categorías distintas
 *
 * @author Gabriel Norambuena
 * @version 1.1
 * @apiNote Adaptado del mvp m4
 */
@Component
public class ProductCategoryDiscountRule implements DiscountRule {

    /**
     * {@inheritDoc}
     * Implementación específica que comprueba que la cantidad total de categorías sea mayor o igual
     * a {@value DiscountConstants#CATEGORIES_THRESHOLD}
     */
    @Override
    public boolean isApplicable(Cart cart) {
        long differentCategories = cart.getItems().stream()
                .map(item -> item.getProduct().getCategory().getId())
                .filter(Objects::nonNull)
                .distinct()
                .count();
        return differentCategories >= DiscountConstants.CATEGORIES_THRESHOLD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal calculateDiscount(Cart cart) {
        return DiscountConstants.CATEGORIES_DISCOUNT_AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Descuento por llevar varias categorías";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCondition() {
        return "Lleva %d categorías distintas".formatted(DiscountConstants.CATEGORIES_THRESHOLD);
    }
}
