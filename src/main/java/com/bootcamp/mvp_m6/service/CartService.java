package com.bootcamp.mvp_m6.service;

import com.bootcamp.mvp_m6.dto.cart.AddToCartDTO;
import com.bootcamp.mvp_m6.dto.cart.CartPricing;
import com.bootcamp.mvp_m6.dto.cart.CartSummaryDTO;
import com.bootcamp.mvp_m6.exceptions.InvalidOperationException;
import com.bootcamp.mvp_m6.mapper.CartMapper;
import com.bootcamp.mvp_m6.model.Cart;
import com.bootcamp.mvp_m6.model.CartItem;
import com.bootcamp.mvp_m6.model.Product;
import com.bootcamp.mvp_m6.model.User;
import com.bootcamp.mvp_m6.repository.CartRepository;
import com.bootcamp.mvp_m6.strategy.discount.DiscountRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para manipular el carrito de compras
 *
 * @author Gabriel Norambuena
 * @version 2.0
 * @apiNote Adaptado del mvp m4
 */
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final List<DiscountRule> discountRules;

    private final ProductService productService;


    /**
     * Obtiene el carro para el usuario que posee la sesión iniciada
     *
     * @param user Usuario loggeado
     * @return Carrito perteneciente al usuario
     * @apiNote Si no se encuentra un carro, se le asigna uno nuevo al usuario autenticado
     */
    @Transactional
    public Cart getCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> assignToUser(user));
    }


    /**
     * Añade un producto al carro actual del usuario
     *
     * @param user Usuario sobre el cual añadir el producto
     * @param dto  Dto con info del producto a agregar
     * @throws InvalidOperationException Cuando se intenta agregar más cantidad del stock disponible
     */
    @Transactional
    public void addProductToCart(User user, AddToCartDTO dto) {

        //primero obtengo el carro del usuario y el producto
        Cart cart = getCart(user);
        Product product = productService.getProduct(dto.productId());


        // verifico si el producto ya lo tengo en el carro
        // si no lo tengo, lo tengo que crear
        // si lo tengo solo le añado la cantidad

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(dto.productId()))
                .findFirst()
                .orElse(null);

        int newQuantity = cartItem == null ? dto.quantity() : cartItem.getQuantity() + dto.quantity();

        if (product.getStock() - newQuantity < 0) {
            throw new InvalidOperationException("No se ha podido agregar el producto '%s' debido a que no queda stock suficiente".formatted(product.getName()));
        }

        if (cartItem != null) {
            cartItem.setQuantity(newQuantity);

        } else {
            //nuevo producto
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .quantity(dto.quantity())
                    .product(product)
                    .build();

            cart.getItems().add(newItem);
        }
    }

    /**
     * Obtiene el resumen de un carrito
     * @param user Usuario relacionado con carrito
     * @return DTO con resumen del carrito
     */
    @Transactional(readOnly = true)
    public CartSummaryDTO getCartSummary(User user) {
        Cart cart = getCart(user);
        CartPricing cartPricing = calculatePricing(cart);

        return cartMapper.toSummaryDTO(cart, cartPricing);
    }

    public int getCarItemCount(User user) {
        Cart cart = getCart(user);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return 0;
        }

        return cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }


    /**
     * Asigna un nuevo carro a un usuario
     *
     * @param user Usuario sobre el cual asignar el carro
     * @return Carrito asignado
     */
    @Transactional
    private Cart assignToUser(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    /**
     * Remueve un producto del carrito
     * @param user Usuario relacionado con el carrito
     * @param productId Id del producto a remover
     */
    @Transactional
    public void removeFromCart(User user, Long productId) {
        Cart cart = getCart(user);

        cart.getItems()
                .removeIf(item -> item.getProduct().getId().equals(productId));
    }

    /**
     * Limpia el carrito de un usuario
     * @param user Usuario relacionado con el carrito
     */
    @Transactional
    public void clearCart(User user) {
        Cart cart = getCart(user);
        cart.getItems().clear();
    }

    /**
     * Calcula el precio de un carrito
     * @param cart Carrito sobre el que calcular
     * @return Precios calculados del carrito
     */
    public CartPricing calculatePricing(Cart cart) {
        List<String> discountConditions = new ArrayList<>();

        //cálculo del subtotal
        BigDecimal subtotal = cart.getItems().stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //cálculo de descuentos
        BigDecimal totalDiscount = BigDecimal.ZERO;

        //recorre todas las reglas de descuento, y en caso de ser aplicable, le sumo el descuento
        for (DiscountRule rule : discountRules) {
            if (rule.isApplicable(cart)) {
                totalDiscount = totalDiscount.add(rule.calculateDiscount(cart));
                discountConditions.add(rule.getCondition());
            }
        }

        //si el descuento es negativo significaría que le aumento el precio, no debería pasar, pero por si acaso...
        if (totalDiscount.compareTo(BigDecimal.ZERO) < 0) {
            totalDiscount = BigDecimal.ZERO;
        }

        BigDecimal discountedPrice = subtotal.multiply(totalDiscount.movePointLeft(2));
        BigDecimal totalFinal = subtotal.subtract(discountedPrice);

        if (totalFinal.compareTo(BigDecimal.ZERO) < 0) {
            totalFinal = BigDecimal.ZERO;
        }

        return new CartPricing(
                subtotal,
                totalDiscount,
                totalFinal,
                discountConditions
        );
    }
}
