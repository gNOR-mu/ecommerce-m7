package com.bootcamp.mvp_m6.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /*Pruebas para /admin */
    @Test
    @DisplayName("Un admin debe tener acceso a /admin (código 200)")
    @WithMockUser(roles = "ADMIN")
    public void adminShouldAccessToAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Un cliente no debe tener acceso a /admin (código 403)")
    @WithMockUser(roles = "CLIENT")
    public void clientShouldNotAccessToAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Alguien no autenticado en /admin es redirigido a login")
    @WithAnonymousUser
    public void anonymousShouldNotAccessToAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin"))
                //lo manda al login
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    /*Pruebas para /admin/products */
    @Test
    @DisplayName("Un admin debe tener acceso a /admin/products (código 200)")
    @WithMockUser(roles = "ADMIN")
    public void adminShouldAccessToAdminProduct() throws Exception {
        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Un cliente no debe tener acceso a /admin/products (código 403)")
    @WithMockUser(roles = "CLIENT")
    public void clientShouldNotAccessToAdminProduct() throws Exception {
        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Alguien no autenticado en /admin/products es redirigido a login")
    @WithAnonymousUser
    public void anonymousShouldNotAccessToAdminProduct() throws Exception {
        mockMvc.perform(get("/admin"))
                //lo manda al login
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    /*Pruebas para /admin/form */
    @Test
    @DisplayName("Un admin debe tener acceso a /admin/products/form (código 200)")
    @WithMockUser(roles = "ADMIN")
    public void adminShouldAccessToAdminProductForm() throws Exception {
        mockMvc.perform(get("/admin/products/form"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Un cliente no debe tener acceso a /admin/products/form (código 403)")
    @WithMockUser(roles = "CLIENT")
    public void clientShouldNotAccessToAdminProductForm() throws Exception {
        mockMvc.perform(get("/admin/products/form"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Alguien no autenticado en /admin/products/form es redirigido a login")
    @WithAnonymousUser
    public void anonymousShouldNotAccessToAdminProductForm() throws Exception {
        mockMvc.perform(get("/products/form"))
                //lo manda al login
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }


}