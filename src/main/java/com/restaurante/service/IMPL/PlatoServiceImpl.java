package com.restaurante.service.IMPL;

import com.restaurante.exception.PlatoNotFoundException;
import com.restaurante.model.domain.Plato;
import com.restaurante.service.PlatoService;
import com.restaurante.validator.PlatoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlatoServiceImpl implements PlatoService {

    private final Map<Long, Plato> platos = new ConcurrentHashMap<>();
    private final AtomicLong contadorId = new AtomicLong(0);

    private final PlatoValidator platoValidator;

    @Override
    public Plato crear(Plato plato) {

        platoValidator.validarCreacion(
                plato,
                platos.values()
        );

        Long id = contadorId.incrementAndGet();

        plato.setId(id);
        platos.put(id, plato);

        log.info(
                "Plato creado correctamente con id: {}",
                id
        );

        return plato;
    }

    @Override
    public Plato obtenerPorId(Long id) {

        Plato plato = platos.get(id);

        if (plato == null) {
            throw new PlatoNotFoundException(id);
        }

        return plato;
    }

    @Override
    public List<Plato> obtenerTodos() {

        return new ArrayList<>(platos.values());
    }

    @Override
    public List<Plato> obtenerDisponibles() {

        return platos.values()
                .stream()
                .filter(Plato::getDisponible)
                .toList();
    }

    @Override
    public Plato actualizar(
            Long id,
            Plato plato) {

        if (!platos.containsKey(id)) {
            throw new PlatoNotFoundException(id);
        }

        platoValidator.validarActualizacion(
                id,
                plato,
                platos.values()
        );

        plato.setId(id);
        platos.put(id, plato);

        log.info(
                "Plato actualizado correctamente con id: {}",
                id
        );

        return plato;
    }

    @Override
    public void eliminar(Long id) {

        if (!platos.containsKey(id)) {
            throw new PlatoNotFoundException(id);
        }

        platos.remove(id);

        log.info(
                "Plato eliminado correctamente con id: {}",
                id
        );
    }
}