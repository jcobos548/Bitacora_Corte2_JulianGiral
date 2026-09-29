package com.restaurante.exception;

public class PlatoNotFoundException extends RuntimeException {

    public PlatoNotFoundException(Long id) {
        super("No se encontró el plato con id: " + id);
    }
}