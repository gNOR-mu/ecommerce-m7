package com.bootcamp.mvp_m6.config;

import com.bootcamp.mvp_m6.enums.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * Autenticador personalizado para manipular el inicio de sesión exitoso.
 * Redirige a los usuarios con rol "ADMIN" a la ruta /admin/products, y a los usuarios con rol "CLIENT" a la ruta /products
 *
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //en caso de agregar más roles tendría que refactorizar

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).filter(Objects::nonNull)
                .anyMatch(role -> role.equals("ROLE_" + Role.ADMIN));

        if (isAdmin) {
            response.sendRedirect("/admin/products");
        } else {
            response.sendRedirect("/products");
        }
    }
}
