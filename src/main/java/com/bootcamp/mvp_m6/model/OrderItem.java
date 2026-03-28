package com.bootcamp.mvp_m6.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/**
 * Modelo orden item de la base de datos.
 * Contiene información respecto a los productos de una orden determinada.
 * @author Gabriel Norambuena
 * @version 1.0
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El precio unitario no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El precio unitario no puede ser inferior a 0.0")
    private BigDecimal unitPrice;

    @NotNull(message = "El sub total no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El sub total no puede ser inferior a 0.0")
    private BigDecimal subTotal;

    @NotNull(message ="La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad mínima no puede ser inferior a 1")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private Order order;


}
