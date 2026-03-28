package com.bootcamp.mvp_m6.service;

import com.bootcamp.mvp_m6.dto.product.AdminProductListDTO;
import com.bootcamp.mvp_m6.dto.product.ProductFormDTO;
import com.bootcamp.mvp_m6.dto.product.ProductInfoDTO;
import com.bootcamp.mvp_m6.dto.product.ProductResumeDTO;
import com.bootcamp.mvp_m6.exceptions.InvalidOperationException;
import com.bootcamp.mvp_m6.exceptions.ResourceNotFoundException;
import com.bootcamp.mvp_m6.mapper.ProductMapper;
import com.bootcamp.mvp_m6.model.Cart;
import com.bootcamp.mvp_m6.model.CartItem;
import com.bootcamp.mvp_m6.model.Product;
import com.bootcamp.mvp_m6.repository.ProductRepository;
import com.bootcamp.mvp_m6.util.UUIDGen;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para los productos
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductMapper productMapper;

    /**
     * Crea un nuevo producto
     *
     * @param dto DTO del producto a crear
     * @return producto creado
     */
    @Transactional
    public Product create(ProductFormDTO dto) {
        validateFields(dto);

        dto.buildFeaturesMap();
        Product product = productMapper.toEntity(dto);
        if (product.getSku() == null || product.getSku().isEmpty()) {
            product.setSku(UUIDGen.generateSimpleSku());
        }

        return productRepository.save(product);
    }

    /**
     * Obtiene los productos más vendidos
     *
     * @return Una lista con los productos más vendidos
     */
    @Transactional(readOnly = true)
    public List<ProductResumeDTO> getTopProducts() {
        return productRepository.getTopProducts();
    }

    /**
     * Obtiene un resumen de todos los productos
     *
     * @return Una lista con el resumen de todos los productos
     */
    @Transactional(readOnly = true)
    public List<ProductResumeDTO> findAllResume() {
        return productRepository.findAllResume();
    }

    /**
     * Busca la información de un producto por ID
     *
     * @param id ID del producto
     * @return Información del producto
     */
    @Transactional(readOnly = true)
    public ProductInfoDTO findInfoById(Long id) {
        return productRepository.findInfoById(id);
    }

    /**
     * Busca todos los productos como administrador
     *
     * @return Una lista de todos los productos
     */
    @Transactional(readOnly = true)
    public List<AdminProductListDTO> findAll() {
        return productRepository.findAllAdmin();
    }

    /**
     * Elimina un producto.
     *
     * @param id Id del producto
     * @throws ResourceNotFoundException Cuando no se encuentra la id del producto
     * @apiNote Si el producto tiene ventas lo deja inactivo
     */
    @Transactional
    public void deleteById(Long id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        if (existing.getOrderItems().isEmpty() && existing.getCartItem().isEmpty()) {
            productRepository.deleteById(id);

        } else {
            //quito el producto de cualquier carrito que lo tenga
            existing.removeAllCartItems();

            existing.setActive(false);
            productRepository.save(existing);
        }


    }


    /**
     * Busca un producto por ID
     *
     * @param id ID del producto
     * @return Producto con la id coincidente
     * @throws ResourceNotFoundException Cuando no se encuentra la id del producto
     */
    @Transactional(readOnly = true)
    public ProductFormDTO getProductForm(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));


        return productMapper.toDTO(product);
    }

    /**
     * Obtiene un producto a partir de su id
     *
     * @param id id del producto
     * @return Producto con la id coincidente
     * @throws ResourceNotFoundException Cuando no se encuentra la id del producto
     */
    @Transactional(readOnly = true)
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

    }

    /**
     * Edita un producto
     *
     * @param dto Producto a editar
     * @throws ResourceNotFoundException Cuando no se encuentra la id del producto
     */
    @Transactional
    public void update(Long id, ProductFormDTO dto) {
        validateFields(dto);

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        dto.buildFeaturesMap();

        productMapper.updateEntityFromDTO(dto, existing);

        existing.setCategory(categoryService.getReferenceById(dto.getCategoryId()));
        existing.setBrand(brandService.getReferenceById(dto.getBrandId()));

        productRepository.save(existing);
    }

    /**
     * Busca productos
     *
     * @param searchText Texto a buscar
     * @return Listado con los productos coincidentes
     */
    @Transactional(readOnly = true)
    public List<AdminProductListDTO> search(String searchText) {
        return productRepository.search(searchText);
    }

    /**
     * Valida que todos los productos del carrito sean válidos y luego reduce el stock
     *
     * @param cart Carrito contenedor de los productos
     * @throws InvalidOperationException Cuando el producto está inactivo
     * @throws InvalidOperationException Cuando no hay stock disponible
     */
    @Transactional
    public void validateAndReduceStock(Cart cart) {
        List<Long> productIds = cart.getItems().stream()
                .map(item -> item.getProduct().getId())
                .toList();

        List<Product> allCartProducts = productRepository.findAllById(productIds);

        Map<Long, Product> mapProducts = allCartProducts.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        for (CartItem item : cart.getItems()) {
            Product product = mapProducts.get(item.getProduct().getId());

            int newStock = product.getStock() - item.getQuantity();

            if (!product.isActive()) {
                throw new InvalidOperationException("El producto '%s' se encuentra inactivo".formatted(product.getName()));
            }

            if (newStock < 0) {
                throw new InvalidOperationException("No hay stock disponible para: " + product.getName()
                        + ", Requerido: " + item.getQuantity()
                        + ", En stock: " + product.getStock()
                );
            }

            product.setStock(newStock);

            productRepository.save(product);
        }
    }


    /**
     * Valida los campos de un producto
     *
     * @param product Producto a validar
     * @throws InvalidOperationException cuando el nombre del producto está vacío
     * @throws  InvalidOperationException cuando el precio es <= 0
     * @throws  InvalidOperationException Cuando el stock es negativo
     * @throws ResourceNotFoundException cuando no existe la id de categoría
     * @throws ResourceNotFoundException cuando no existe la id de marca
     */
    private void validateFields(ProductFormDTO product) {
        if (product.getName() == null || product.getName().isBlank()) {
            throw new InvalidOperationException("El nombre del producto no puede ser vacío ni estar en blanco");
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("El precio debe ser mayor a 0");
        }

        if (product.getStock() < 0) {
            throw new InvalidOperationException("El stock no puede ser negativo");
        }

        if (product.getCategoryId() == null || !categoryService.existsById(product.getCategoryId())) {
            throw new ResourceNotFoundException("Category", product.getCategoryId());
        }

        if (product.getBrandId() == null || !brandService.existsById(product.getBrandId())) {
            throw new ResourceNotFoundException("Brand", product.getBrandId());
        }
    }
}
