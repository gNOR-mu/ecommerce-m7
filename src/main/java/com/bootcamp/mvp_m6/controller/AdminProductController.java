package com.bootcamp.mvp_m6.controller;

import com.bootcamp.mvp_m6.dto.product.ProductFormDTO;
import com.bootcamp.mvp_m6.service.BrandService;
import com.bootcamp.mvp_m6.service.CategoryService;
import com.bootcamp.mvp_m6.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para la administración de productos como administrador
 */
@Controller
@RequestMapping("/admin/products")
@Slf4j
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    /**
     * Muestra la página de administración de productos
     *
     * @param model Model entregado por Spring Boot
     * @return nombre de la plantilla Thymeleaf a renderizar (admin/product)
     */
    @GetMapping
    public String productManagement(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/product";
    }

    /**
     * Muestra el formulario de creación o actualización de un producto.
     * <p></p>
     * Cuando se suministra el parámetro "id", se busca el objeto en la base de datos.
     * En caso de no se suministre el parámetro "Id", se entrega un objeto vacío
     *
     * @param id    Id (opcional) del producto
     * @param model Model entregado por Spring Boot
     * @return nombre de la plantilla Thymeleaf a renderizar (admin/productForm)
     */
    @GetMapping("/form")
    public String newProduct(
            @RequestParam(required = false) Long id,
            Model model) {

        ProductFormDTO product = id == null
                ? new ProductFormDTO()
                : productService.getProductForm(id);

        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("product", product);

        return "admin/productForm";
    }

    /**
     *
     * @param id                 Identificación del objeto a eliminar
     * @param redirectAttributes RedirectAttributes otorgado por Spring Boot
     * @return nombre de la plantilla Thymeleaf a redireccionar (redirect:/admin/products)
     * @apiNote <p>
     * Se añade "successMessage" como FlashAttribute si el producto es eliminado exitosamente.
     * Se añade "errorMessage" como FlashAttribute si ocurre un error al eliminar el producto
     * </p>
     */
    @DeleteMapping("/{id}")
    public String deleteProduct(
            @PathVariable @NotNull Long id,
            RedirectAttributes redirectAttributes) {

        try {
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Producto eliminado.");

        } catch (Exception e) {
            log.error("Error al intentar eliminar un producto: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Ha ocurrido un error al intentar eliminar el producto");
        }
        return "redirect:/admin/products";
    }

    /**
     * Crea un nuevo producto
     *
     * @param dto                DTO con información del producto a crear
     * @param bindingResult      BindingResult otorgado por Spring Boot
     * @param redirectAttributes RedirectAttributes otorgado por Spring Boot
     * @param model              Model otorgado por Spring Boot
     * @return nombre de la plantilla Thymeleaf a renderizar, si hay error: admin/productForm, en caso exitoso: redirección
     * hacia admin/products
     * @apiNote <p>
     * Se añade "successMessage" como FlashAttribute si el producto es creado exitosamente.
     * Se añade "errorMessage" como FlashAttribute si ocurre un error al crear el producto
     * </p>
     */
    @PostMapping
    public String saveProduct(
            @Valid @ModelAttribute("product") ProductFormDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            //los vuelvo a agregar
            model.addAttribute("brands", brandService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            return "admin/productForm";
        }

        try {
            productService.create(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Producto %s creado.".formatted(dto.getName()));
        } catch (Exception e) {
            log.error("Error al intentar crear un producto: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Ha ocurrido un error al intentar crear el producto");
        }

        return "redirect:/admin/products";
    }

    /**
     * Actualiza un producto
     *
     * @param dto                DTO con información del producto a crear
     * @param id                 Id del producto a actualizar
     * @param bindingResult      BindingResult otorgado por Spring Boot
     * @param redirectAttributes RedirectAttributes otorgado por Spring Boot
     * @return nombre de la plantilla Thymeleaf a renderizar, si hay error: admin/productForm, en caso exitoso: redirección
     * hacia admin/products
     * @apiNote <p>
     * Se añade "successMessage" como FlashAttribute si el producto es actualizado exitosamente.
     * Se añade "errorMessage" como FlashAttribute si ocurre un error al actualizar el producto
     * </p>
     */
    @PutMapping("/{id}")
    public String updateProduct(
            @Valid @ModelAttribute("product") ProductFormDTO dto,
            BindingResult bindingResult,
            @PathVariable @NotNull Long id,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/productForm";
        }
        try {
            productService.update(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Producto actualizado correctamente.");
        } catch (Exception e) {
            log.error("Error al intentar actualizar un producto: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Ha ocurrido un error al intentar actualizar el producto");
        }
        return "redirect:/admin/products";
    }

    /**
     * Busca productos por nombre/categoría/marca
     *
     * @param model      Modelo otorgado por Spring Boot
     * @param searchText Texto a buscar
     * @return nombre de la plantilla Thymeleaf a renderizar (admin/product)
     */
    @GetMapping("/search")
    public String search(
            Model model,
            @RequestParam @NotNull String searchText) {

        model.addAttribute("products", productService.search(searchText));
        return "admin/product";
    }
}
