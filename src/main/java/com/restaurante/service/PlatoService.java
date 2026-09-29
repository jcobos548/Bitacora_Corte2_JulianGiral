package com.restaurante.service;

import com.restaurante.model.domain.Plato;

import java.util.List;

public interface PlatoService {

    Plato crear(Plato plato, Long categoriaId);

    Plato obtenerPorId(Long id);

    List<Plato> obtenerTodos();

    List<Plato> obtenerDisponibles();

    Plato actualizar(Long id, Plato plato, Long categoriaId);

    void eliminar(Long id);
}