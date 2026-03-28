package com.bootcamp.mvp_m6.controller;

import com.bootcamp.mvp_m6.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador de inicio
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ProductService productService;

    /**
     * Muestra la página de inicio.
     * @return nombre de la plantilla Thymeleaf a renderizar (home)
     */
    @GetMapping
    public String home(Model model) {
        model.addAttribute("topProducts", productService.getTopProducts());
        return "home";
    }
}
