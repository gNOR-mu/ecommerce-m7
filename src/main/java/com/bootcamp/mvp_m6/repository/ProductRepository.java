package com.bootcamp.mvp_m6.repository;

import com.bootcamp.mvp_m6.dto.product.AdminProductListDTO;
import com.bootcamp.mvp_m6.dto.product.ProductInfoDTO;
import com.bootcamp.mvp_m6.dto.product.ProductResumeDTO;
import com.bootcamp.mvp_m6.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operar sobre Product en la base de datos
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Busca un resumen de todos los productos activos
     * @return Lista DTO con todos los productos ({@link  ProductResumeDTO})
     */
    @Query("""
            SELECT new com.bootcamp.mvp_m6.dto.product.ProductResumeDTO(id, name, shortDescription, urlImage, price)
            FROM Product p
            WHERE p.active = true
            """)
    List<ProductResumeDTO> findAllResume();

    /**
     * Busca todos los productos activos como administrador
     * @return Lista DTO de todos los productos activos ({@link AdminProductListDTO})
     */
    @Query("""
            SELECT new com.bootcamp.mvp_m6.dto.product.AdminProductListDTO(
                p.id,
                p.price,
                p.name,
                c.name,
                b.name,
                p.stock)
            FROM Product p
            JOIN p.brand b
            JOIN p.category c
            WHERE p.active = true
            """)
    List<AdminProductListDTO> findAllAdmin();

    /**
     * Busca información de un producto a partir de su id
     * @param id Id del producto
     * @return DTO del producto coincidente
     */
    @Query("""
            SELECT new com.bootcamp.mvp_m6.dto.product.ProductInfoDTO(
                id,
                price,
                features,
                name,
                urlImage,
                description,
                shortDescription)
            FROM Product
            WHERE id = :id
            AND active = true
            """)
    ProductInfoDTO findInfoById(@Param("id") Long id);


    /**
     * Obtiene un listado del top 3 productos más vendidos
     *
     * @return Listado con los 3 productos más vendidos
     */
    @Query("""
            SELECT new com.bootcamp.mvp_m6.dto.product.ProductResumeDTO(p.id, p.name, p.shortDescription, p.urlImage, p.price)
            FROM OrderItem oi
            JOIN oi.product p
            WHERE p.active = true
            GROUP BY p.id, p.name, p.shortDescription, p.urlImage, p.price
            ORDER BY SUM(oi.quantity) DESC
            LIMIT 3
            """)
    List<ProductResumeDTO> getTopProducts();

    /**
     * Busca productos que contengan la palabra clave por nombre de producto, nombre de categoría o nombre de marca
     *
     * @param searchText Texto a buscar
     * @return Lista de los productos que contienen el texto especificado
     * @apiNote Ignora mayúsculas/minúsculas
     */
    @Query("""
            SELECT new com.bootcamp.mvp_m6.dto.product.AdminProductListDTO(
                p.id,
                p.price,
                p.name,
                c.name,
                b.name,
                p.stock)
            FROM Product p
            JOIN p.brand b
            JOIN p.category c
            WHERE p.name ILIKE %:searchText%
            AND p.active = true
            OR c.name ILIKE %:searchText%
            or b.name ILIKE %:searchText%
            """)
    List<AdminProductListDTO> search(@Param("searchText") String searchText);

    /**
     * Busca un producto por el sku
     * @param sku Sku a buscar
     * @return Producto con el sku
     */
    Optional<Product> findBySku(String sku);
}
