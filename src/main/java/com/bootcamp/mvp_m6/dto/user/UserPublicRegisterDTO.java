package com.bootcamp.mvp_m6.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO para el registro de un usuario público
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPublicRegisterDTO {

    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;

    @NotBlank(message = "El apellido no puede estar en blanco")
    private String lastName;

    @NotNull(message = "El correo no puede ser nulo")
    @Email(message = "Correo inválido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar en blanco")
    @Size(min = 8, message = "El largo mínimo para la contraseña son 8 caracteres")
    @Size(max = 100, message = "El largo máximo para la contraseña son 100 caracteres")
    private String password;
}
