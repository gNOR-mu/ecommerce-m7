package com.bootcamp.mvp_m6.controller;

import com.bootcamp.mvp_m6.dto.cart.AddToCartDTO;
import com.bootcamp.mvp_m6.service.ProductService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para los productos
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Muestra la página de productos.
     *
     * @param model Model entregado por Spring Boot
     * @return nombre de la plantilla Thymeleaf a renderizar (products)
     */
    @GetMapping
    public String products(Model model) {
        model.addAttribute("products", productService.findAllResume());
        return "products";
    }

    /**
     * Muestra la página de un producto.
     *
     * @param model Model entregado por Spring Boot
     * @param id    Id del producto a cargar
     * @return nombre de la plantilla Thymeleaf a renderizar (productId)
     */
    @GetMapping("/{id}")
    public String products(Model model,
                           @NotNull @PathVariable Long id) {
        model.addAttribute("product", productService.findInfoById(id));
        model.addAttribute("addToCart", new AddToCartDTO(id, 1));
        return "productId";
    }
}
