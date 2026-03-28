package com.bootcamp.mvp_m6.controller;

import com.bootcamp.mvp_m6.model.User;
import com.bootcamp.mvp_m6.service.CheckoutService;
import com.bootcamp.mvp_m6.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para realizar el pago
 */
@Controller
@RequestMapping("/checkout")
@Slf4j
public class CheckoutController {

    @Autowired
    private UserService userService;

    @Autowired
    private CheckoutService checkoutService;

    /**
     * Muestra la página checkout.
     * @return nombre de la plantilla Thymeleaf a renderizar (checkout)
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String checkout() {
        return "checkout";
    }

    /**
     * Realiza el checkout
     * @param userDetails UserDetails entregado por Spring Boot
     * @param redirectAttributes RedirectAttributes entregado por Spring Boot
     * @return nombre de la plantilla Thymeleaf a redireccionar (redirect:/checkout)
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public String checkout(@AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getByEmail(userDetails.getUsername());
            checkoutService.checkout(user);
        } catch (Exception e) {
            log.error("Ha ocurrido un error al procesar la compra: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Ha ocurrido un error al procesar la compra");
        }

        return "redirect:/checkout";
    }


}
