package com.bootcamp.mvp_m6.strategy.discount.rules;

import com.bootcamp.mvp_m6.constants.DiscountConstants;
import com.bootcamp.mvp_m6.model.Cart;
import com.bootcamp.mvp_m6.model.CartItem;
import com.bootcamp.mvp_m6.strategy.discount.DiscountRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Regla para los descuentos por costo total del carrito
 *
 * @author Gabriel Norambuena
 * @version 2.0
 * @apiNote Adaptado del mvp m4
 */
@Component
public class TotalAmountDiscountRule implements DiscountRule {
    /**
     * {@inheritDoc}
     * Implementación específica que comprueba que el costo total del carro, pre descuentos, sea mayor o igual
     * a {@link DiscountConstants#TOTAL_AMOUNT_THRESHOLD}
     */
    @Override
    public boolean isApplicable(Cart cart) {
        BigDecimal total = calculateTotal(cart);
        return total.compareTo(DiscountConstants.TOTAL_AMOUNT_THRESHOLD) >= 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal calculateDiscount(Cart cart) {
        return DiscountConstants.TOTAL_COST_AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Descuento por valor a pagar";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCondition() {
        return "LLeva %s en valor total del carrito".formatted(DiscountConstants.TOTAL_AMOUNT_THRESHOLD.toPlainString());
    }

    /**
     * Calcula el total de descuentos
     * @param cart Carrito sobre el cual calcular el descuento
     * @return Descuento calculado
     */
    private BigDecimal calculateTotal(Cart cart){
        //si no tengo objetos, no hay descuento
        var items = cart.getItems();

        if(items == null || items.isEmpty()){
            return BigDecimal.ZERO;
        }

        return items.stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
