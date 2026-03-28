package com.bootcamp.mvp_m6.controller;

import com.bootcamp.mvp_m6.dto.cart.AddToCartDTO;
import com.bootcamp.mvp_m6.dto.cart.CartSummaryDTO;
import com.bootcamp.mvp_m6.model.User;
import com.bootcamp.mvp_m6.service.CartService;
import com.bootcamp.mvp_m6.service.ProductService;
import com.bootcamp.mvp_m6.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para el carrito
 */
@Slf4j
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    /**
     * Muestra la página del carrito.
     * @return nombre de la plantilla Thymeleaf a renderizar (cart)
     *
     * @param model Modelo entregado por Spring Boot
     * @param userDetails  UserDetails entregado por Spring Boot
     */
    @GetMapping
    public String cart(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.getByEmail(userDetails.getUsername());

        CartSummaryDTO cart = cartService.getCartSummary(user);
        model.addAttribute("cart", cart);
        return "cart";
    }

    /**
     * Añade un nuevo producto al carro.
     * @return nombre de la plantilla Thymeleaf a renderizar (redirect:/cart), o "productId" si hay un error en el formulario
     *
     * @param userDetails UserDetails entregado por Spring Boot
     * @param model Model entregado por Spring Boot
     * @param bindingResult BindingResult entregado por Spring Boot
     * @param dto DTO con información del producto para añadir al carro
     */
    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(
            @Valid @ModelAttribute("addToCart") AddToCartDTO dto,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productService.findInfoById(dto.productId()));
            model.addAttribute("addToCart", dto);
            return "productId";
        }

        try {
            User user = userService.getByEmail(userDetails.getUsername());
            cartService.addProductToCart(user, dto);
        } catch (Exception e) {
            log.error("Error al añadir un producto al carro: {}", e.getMessage());
            model.addAttribute("errorMessage", "No se ha podido agregar debido a que no hay stock suficiente :(");
            model.addAttribute("product", productService.findInfoById(dto.productId()));
            model.addAttribute("addToCart", dto);
            return "productId";
        }
        return "redirect:/cart";
    }

    /**
     * Elimina completamente un producto del carrito
     * @param id Id del producto a eliminar del carrito
     * @param userDetails UserDetails entregado por Spring Boot
     */
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("isAuthenticated()")
    public String remove(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
         try{
             User user = userService.getByEmail(userDetails.getUsername());
             cartService.removeFromCart(user, id);
         }catch (Exception e){
             log.error("Ha ocurrido un error al intentar eliminar un producto del carrito: {}", e.getMessage());
         }
        return "redirect:/cart";
    }

}
