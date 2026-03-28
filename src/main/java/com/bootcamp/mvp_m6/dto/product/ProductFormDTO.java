package com.bootcamp.mvp_m6.dto.product;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO para el formulario de producto
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFormDTO {

    private Long id;

    @NotNull(message = "La id de la categoría no puede ser nula")
    private Long categoryId;

    @NotNull(message = "La id de la marca no puede ser nula")
    private Long brandId;

    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "1.0", message = "El precio no puede ser inferior a 1.0")
    private BigDecimal price;

    @Builder.Default
    private Map<String, Object> features = new HashMap<>();

    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;

    @NotBlank(message = "La URL no puede estar vacía")
    @URL(message = "URL no valida")
    private String urlImage;

    private String sku;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 1000, message = "El largo máximo para la descripción son 1000 caracteres")
    private String description;

    @NotBlank(message = "La descripción corta no puede estar vacía")
    @Size(max = 50, message = "El largo máximo para la descripción corta son 50 caracteres")
    private String shortDescription;

    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock no puede ser inferior a 0")
    private int stock;


    // Claves y valores de las características, adaptado del controlador del mvp5 aquí
    @Builder.Default
    private List<String> featureKeys = new ArrayList<>();

    @Builder.Default
    private List<String> featureValues = new ArrayList<>();

    public void buildFeaturesMap() {
        if (featureKeys != null && featureValues != null) {
            this.features = new HashMap<>();

            for (int i = 0; i < featureKeys.size(); i++) {
                String key = featureKeys.get(i).trim();
                String value = featureValues.get(i).trim();

                if (!key.isEmpty() && !value.isEmpty()) {
                    features.put(key, value);
                }
            }
        }
    }
}
