package com.bootcamp.mvp_m6.config.seeder;

import com.bootcamp.mvp_m6.dto.cart.AddToCartDTO;
import com.bootcamp.mvp_m6.dto.product.ProductFormDTO;
import com.bootcamp.mvp_m6.dto.user.UserPrivateRegisterDTO;
import com.bootcamp.mvp_m6.dto.user.UserPublicRegisterDTO;
import com.bootcamp.mvp_m6.enums.Role;
import com.bootcamp.mvp_m6.model.*;
import com.bootcamp.mvp_m6.repository.BrandRepository;
import com.bootcamp.mvp_m6.repository.OrderRepository;
import com.bootcamp.mvp_m6.repository.ProductRepository;
import com.bootcamp.mvp_m6.repository.UserRepository;
import com.bootcamp.mvp_m6.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * Inicializador de datos para la base de datos
 */
@Component
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        seed();
    }


    /**
     * Inicializa los datos en la BD
     */
    private void seed() {
        //parece una aberración, cambiar cuando haya tiempo

        /*Usuario administrado*/
        UserPrivateRegisterDTO admin = UserPrivateRegisterDTO.builder()
                .email("admin@email.cl")
                .password("admin1234")
                .lastName("Pérez")
                .name("admin")
                .role(Role.ADMIN)
                .build();

        /*Usuario normal*/
        UserPublicRegisterDTO user = UserPublicRegisterDTO.builder()
                .email("user@email.cl")
                .password("user1234")
                .lastName("Pérez")
                .name("usuario")
                .build();

        if (!userRepository.existsByEmail(admin.getEmail())) {
            userService.createPrivateUser(admin);
            log.info("Seeder: Admin añadido");
        }

        if (!userRepository.existsByEmail(user.getEmail())) {
            userService.createPublicUser(user);
            log.info("Seeder: usuario añadido");
        }

        Brand maui = brandService.findOrCreate("Maui");
        Brand head = brandService.findOrCreate("Head");
        Brand rayBand = brandService.findOrCreate("Ray-Ban");
        Brand totem = brandService.findOrCreate("Totem");

        Category ropa = categoryService.findOrCreate("Ropa");
        Category expedicion = categoryService.findOrCreate("Expedición");
        Category accesorios = categoryService.findOrCreate("Accesorios");
        Category deporte = categoryService.findOrCreate("Deporte");

        Product mochila = productRepository.findBySku("MO-1").orElseGet(() -> productService.create(ProductFormDTO.builder()
                .sku("MO-1")
                .categoryId(expedicion.getId())
                .brandId(head.getId())
                .price(new BigDecimal("85000.0"))
                .shortDescription("Mochila de expedición de 60L. Ideal para viajes.")
                .name("Mochila Head Trekking 60L")
                .urlImage("https://c.pxhere.com/photos/e1/74/backpack_hiking_backpack_hiking_mountains_forest_vietnam_nature_water_bottle-1395064.jpg!d")
                .stock(15)
                .description("""
                        Esta mochila de expedición está diseñada para los aventureros más exigentes. Confeccionada en Nylon de alta resistencia, garantiza durabilidad y protección
                            contra los elementos. Su capacidad de 60 litros te permite llevar todo lo necesario para tus travesías, mientras que su diseño ergonómico asegura una distribución
                            equilibrada del peso y un confort óptimo durante largas caminatas. El color azul vibrante no solo le da un toque de estilo, sino que también mejora la visibilidad en
                            entornos naturales. Además, cuenta con múltiples compartimentos y correas ajustables para organizar tu equipo de manera eficiente. Ya sea para un trekking de varios días
                            o una escalada desafiante, esta mochila es tu compañera ideal.
                        """)
                .features(Map.of(
                        "capacidad", "60 Litros",
                        "Impermeable", "Sí",
                        "Bolsillos", 5
                ))
                .build()
        ));


        Product bici = productRepository.findBySku("BI-0").orElseGet(() ->productService.create(ProductFormDTO.builder()
                .categoryId(deporte.getId())
                .sku("BI-0")
                .brandId(totem.getId())
                .price(new BigDecimal("220000"))
                .shortDescription("Bicicleta de montaña rodado 29, 27 velocidades")
                .name("Bicicleta Totem W860 Aro 29")
                .urlImage("https://upload.wikimedia.org/wikipedia/commons/9/96/Orbea_Occam_2020.jpg")
                .stock(80)
                .description("""
                        Esta Mountain Bike está diseñada para los amantes de la aventura y la velocidad. Con un cuadro de Aluminio ligero y resistente, esta bicicleta te ofrece la agilidad y
                            durabilidad que necesitas para conquistar cualquier sendero. Equipada con 27 velocidades, te permite adaptar tu pedaleo a cualquier tipo de terreno, desde subidas empinadas
                            hasta descensos rápidos. Los frenos de Disco Hidráulico garantizan una frenada potente y segura en todas las condiciones climáticas, mientras que su suspensión delantera
                            y trasera absorbe los impactos, proporcionando un viaje suave y controlado. Con un rodado de 29 pulgadas, esta bicicleta te ofrece una mayor estabilidad y tracción,
                            ideal para explorar nuevos caminos y superar tus límites.
                        """)
                .features(Map.of(
                        "Aro", 29,
                        "Frenos", "Hidráulico",
                        "Velocidades", 27,
                        "Cuadro", "Aluminio"
                ))
                .build()
        ));

        Product carpa = productRepository.findBySku("TI-0").orElseGet(() ->productService.create(ProductFormDTO.builder()
                .categoryId(expedicion.getId())
                .sku("TI-0")
                .brandId(maui.getId())
                .price(new BigDecimal("145000"))
                .shortDescription("Tienda de campaña resistente a lluvias")
                .name("Carpa Maui 4 Personas")
                .urlImage("https://upload.wikimedia.org/wikipedia/commons/b/bb/Husky_Tent_near_scout_camp_near_Kouty%2C_T%C5%99eb%C3%AD%C4%8D_District.jpg")
                .stock(45)
                .description("""
                        Carpa tipo iglú resistente a vientos moderados y lluvias. Fácil de armar e ideal para acampar en
                         familia o con amigos en la naturaleza.
                        """)
                .features(Map.of(
                        "Capacidad", 4,
                        "Estaciones", 3,
                        "Columnas agua", "3000mm",
                        "Peso", "4.5kg"
                ))
                .build()
        ));

        Product chaqueta = productRepository.findBySku("CH-0").orElseGet(() ->productService.create(ProductFormDTO.builder()
                .categoryId(ropa.getId())
                .sku("CH-0")
                .brandId(head.getId())
                .price(new BigDecimal("85000"))
                .shortDescription("Chaqueta resistente al agua")
                .name("Chaqueta Cortavientos")
                .urlImage("https://upload.wikimedia.org/wikipedia/commons/4/46/Windbreaker_Jacket%2C_Hood_Outside.jpg")
                .stock(15)
                .description("""
                        Chaqueta ligera y resistente al agua, perfecta para actividades al aire libre o para protegerse
                         del clima cambiante.
                        """)
                .features(Map.of(
                        "Talla", "L",
                        "Color", "Gris",
                        "Impermeable", "Sí",
                        "Bolsillos con cierre", 2
                ))
                .build()
        ));

        Product gafas = productRepository.findBySku("GA-0").orElseGet(() ->productService.create(ProductFormDTO.builder()
                .categoryId(accesorios.getId())
                .sku("GA-0")
                .brandId(rayBand.getId())
                .price(new BigDecimal("115000"))
                .shortDescription("Gafas de sol con protección UV400")
                .name("Gafas Ray-Ban Aviator")
                .urlImage("https://upload.wikimedia.org/wikipedia/commons/5/58/Beach-sand-summer-46710.jpg")
                .stock(24)
                .description("""
                        El diseño clásico que nunca pasa de moda. Protección total contra los rayos UV con un estilo 
                        inconfundible.
                        """)
                .features(Map.of(
                        "Polarizado", "Sí",
                        "Filtro", "UV400",
                        "Color cristal", "Verde",
                        "Material marco", "Metal"
                ))
                .build()
        ));


        if (!orderRepository.hasData()) {
            Optional<User> firstClientUser = userRepository.findByEmail(user.getEmail());
            if (firstClientUser.isEmpty()) {
                return;
            }
            cartService.addProductToCart(firstClientUser.get(), new AddToCartDTO(gafas.getId(), 1));
            cartService.addProductToCart(firstClientUser.get(), new AddToCartDTO(mochila.getId(), 3));
            cartService.addProductToCart(firstClientUser.get(), new AddToCartDTO(carpa.getId(), 2));
            cartService.addProductToCart(firstClientUser.get(), new AddToCartDTO(bici.getId(), 1));

            checkoutService.checkout(firstClientUser.get());
            log.info("Seeder: Primera venta concretada");
        }


    }
}
