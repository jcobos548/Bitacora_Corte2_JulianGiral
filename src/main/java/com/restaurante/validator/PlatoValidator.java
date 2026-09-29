package com.restaurante.validator;

import com.restaurante.exception.PlatoConflictException;
import com.restaurante.model.domain.Plato;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PlatoValidator {

    public void validarCreacion(Plato plato, Collection<Plato> platosExistentes) {

        boolean nombreExiste = platosExistentes.stream()
                .anyMatch(platoExistente ->
                        platoExistente.getNombre().equalsIgnoreCase(plato.getNombre())
                );

        if (nombreExiste) {
            throw new PlatoConflictException(
                    "Ya existe un plato con el nombre: " + plato.getNombre()
            );
        }
    }

    public void validarActualizacion(
            Long id,
            Plato plato,
            Collection<Plato> platosExistentes) {

        boolean nombreExiste = platosExistentes.stream()
                .anyMatch(platoExistente ->
                        !platoExistente.getId().equals(id)
                                && platoExistente.getNombre().equalsIgnoreCase(plato.getNombre())
                );

        if (nombreExiste) {
            throw new PlatoConflictException(
                    "Ya existe otro plato con el nombre: " + plato.getNombre()
            );
        }
    }
}