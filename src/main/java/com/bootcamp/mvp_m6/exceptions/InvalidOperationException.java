package com.bootcamp.mvp_m6.exceptions;

/**
 * Excepción personalizada para problemas asociados con operaciones no permitidas o inválidas.
 * @author Gabriel Norambuena
 * @version 1.0
 */
public class InvalidOperationException extends RuntimeException {

    /**
     * Lanza un {@link RuntimeException}
     *
     * @param msg Error a mostrar.
     */
    public InvalidOperationException(String msg) {
        super(msg);
    }
}
