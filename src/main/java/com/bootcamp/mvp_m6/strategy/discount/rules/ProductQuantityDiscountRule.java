package com.bootcamp.mvp_m6.strategy.discount.rules;

import com.bootcamp.mvp_m6.constants.DiscountConstants;
import com.bootcamp.mvp_m6.model.Cart;
import com.bootcamp.mvp_m6.model.CartItem;
import com.bootcamp.mvp_m6.strategy.discount.DiscountRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Regla para los descuentos por cantidad de productos
 *
 * @author Gabriel Norambuena
 * @version 1.1
 * @apiNote Adaptado del mvp m4
 */
@Component
public class ProductQuantityDiscountRule implements DiscountRule {

    /**
     * {@inheritDoc}
     * Implementación específica que comprueba que la cantidad total de productos sea mayor o igual
     * a {@value DiscountConstants#QUANTITY_THRESHOLD}
     */
    @Override
    public boolean isApplicable(Cart cart) {
        int quantity = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .filter(Objects::nonNull)
                .sum();
        return quantity >= DiscountConstants.QUANTITY_THRESHOLD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal calculateDiscount(Cart cart) {
        return DiscountConstants.QUANTITY_DISCOUNT_AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Descuento por cantidad de productos";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCondition() {
        return "LLeva %d productos".formatted(DiscountConstants.QUANTITY_THRESHOLD);
    }
}
