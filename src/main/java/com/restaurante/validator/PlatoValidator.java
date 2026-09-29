package com.restaurante.validator;

import com.restaurante.exception.PlatoConflictException;
import com.restaurante.model.domain.Plato;
import com.restaurante.persistence.entity.PlatoEntity;
import com.restaurante.persistence.repository.PlatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlatoValidator {

    private final PlatoRepository platoRepository;

    public void validarCreacion(Plato plato) {

        boolean nombreExiste =
                platoRepository.existsByNombreIgnoreCase(plato.getNombre());

        if (nombreExiste) {
            throw new PlatoConflictException(
                    "Ya existe un plato con el nombre: " + plato.getNombre());
        }
    }

    public void validarActualizacion(Long id, Plato plato) {

        Optional<PlatoEntity> platoExistente =
                platoRepository.findByNombreIgnoreCase(plato.getNombre());

        if (platoExistente.isPresent()
                && !platoExistente.get().getId().equals(id)) {

            throw new PlatoConflictException(
                    "Ya existe otro plato con el nombre: " + plato.getNombre());
        }
    }
}