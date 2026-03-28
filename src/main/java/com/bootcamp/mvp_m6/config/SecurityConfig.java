package com.bootcamp.mvp_m6.config;

import com.bootcamp.mvp_m6.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler authHandler;

    /**
     * Configuración de cadena de filtros de seguridad para la app
     * <ul>
     *     <li>Permite para todos: /, /signup, /assets/**</li>
     *     <li>Permite para los roles ADMIN y CLIENT: /products, /cart</li>
     *     <li>Permite solo para admin: /admin/**</li>
     *     <li>Cualquier otra ruta debe estar autenticada</li>
     * </ul>
     * @param http Objeto {@link  HttpSecurity} proporcionado por Spring Boot
     * @return Cadena de filtros construida ({@link SecurityFilterChain})
     * @throws Exception Si ocurre algún error en la construcción de la cadena de filtros
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/signup", "/assets/**").permitAll()
                        .requestMatchers("/products", "/cart").hasAnyRole(Role.ADMIN.name(), Role.CLIENT.name())
                        .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email") //cambia el username a email
                        .successHandler(authHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                );
        return http.build();
    }

    /**
     * Bean para el PasswordEncoder, utiliza {@link  BCryptPasswordEncoder}
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
