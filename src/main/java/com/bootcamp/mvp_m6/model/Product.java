package com.bootcamp.mvp_m6.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Modelo producto de la base de datos.
 * @author Gabriel Norambuena
 * @version 1.0
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products", indexes = {
        @Index(name = "idx_product_name", columnList = "name"),
        @Index(name = "idx_product_active", columnList = "active")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "1.0", message = "El precio no puede ser inferior a 1.0")
    private BigDecimal price;

    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;

    @NotBlank(message = "La URL no puede estar vacía")
    @URL(message = "URL no valida")
    private String urlImage;

    @NotBlank(message = "La descripción corta no puede estar vacía")
    @Size(max = 50, message = "El largo máximo para la descripción corta son 50 caracteres")
    private String shortDescription;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 1000, message = "El largo máximo para la descripción son 1000 caracteres")
    private String description;

    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock no puede ser inferior a 0")
    private Integer stock;

    @Column(unique = true, nullable = false, updatable = false)
    private String sku;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> features = new HashMap<>();

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Builder.Default
    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<OrderItem> orderItems = new HashSet<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<CartItem> cartItem = new HashSet<>();


    /**
     * Desvincula el producto de cualquier cartItem que lo tenga
     */
    public void removeAllCartItems() {
        for (CartItem item : this.cartItem) {
            item.setProduct(null);
        }
        this.cartItem.clear();
    }


}
