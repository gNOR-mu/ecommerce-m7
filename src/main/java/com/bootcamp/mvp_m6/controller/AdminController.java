package com.bootcamp.mvp_m6.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la administración
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * Muestra el panel de administración.
     * @return nombre de la plantilla Thymeleaf a renderizar (admin/panel)
     */
    @GetMapping
    public String panel() {
        return "admin/panel";
    }

}
