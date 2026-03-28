package com.bootcamp.mvp_m6.exceptions;

/**
 * Excepción personalizada para problemas asociados con un recurso que ya existe.
 * @author Gabriel Norambuena
 * @version 1.0
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    /**
     * Lanza un {@link RuntimeException}
     *
     * @param msg Error a mostrar.
     */
    public ResourceAlreadyExistsException(String msg) {
        super(msg);
    }

    /**
     * Lanza un {@link RuntimeException}.
     *
     * @param resource Nombre del recurso.
     * @param field    Nombre del campo
     * @param value    Nombre del valor
     */
    public ResourceAlreadyExistsException(String resource, String field, Object value) {
        super("%s con %s '%s' ya existe".formatted(resource, field, value));
    }
}
