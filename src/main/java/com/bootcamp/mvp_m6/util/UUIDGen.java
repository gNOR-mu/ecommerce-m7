package com.bootcamp.mvp_m6.util;

import java.util.UUID;

/**
 * Clase de utilidad para generar códigos UUID
 */
public class UUIDGen {

    /**
     * Genera un sku simple
     * @return UUID generado
     */
    public static String generateSimpleSku() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

}
