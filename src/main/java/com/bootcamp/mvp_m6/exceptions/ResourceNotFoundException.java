package com.bootcamp.mvp_m6.exceptions;

/**
 * Excepción personalizada para problemas asociados con un recurso no encontrado.
 * @author Gabriel Norambuena
 * @version 1.0
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Lanza un {@link RuntimeException}
     * @param msg Error a mostrar.
     */
    public ResourceNotFoundException(String msg){
        super(msg);
    }

    /**
     * Lanza un {@link RuntimeException}.
     * @param resource Nombre del recurso.
     * @param id Identificación del recurso

     */
    public ResourceNotFoundException(String resource, Long id) {
        super("%s no encontrado con id : %d".formatted(resource, id));
    }
}
