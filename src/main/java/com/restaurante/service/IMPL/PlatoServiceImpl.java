package com.restaurante.service.IMPL;

import com.restaurante.exception.PlatoNotFoundException;
import com.restaurante.mapper.PlatoPersistenceMapper;
import com.restaurante.model.domain.Plato;
import com.restaurante.persistence.entity.CategoriaEntity;
import com.restaurante.persistence.entity.PlatoEntity;
import com.restaurante.persistence.repository.CategoriaRepository;
import com.restaurante.persistence.repository.PlatoRepository;
import com.restaurante.service.PlatoService;
import com.restaurante.validator.PlatoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlatoServiceImpl implements PlatoService {

    private final PlatoRepository platoRepository;
    private final CategoriaRepository categoriaRepository;
    private final PlatoPersistenceMapper platoPersistenceMapper;
    private final PlatoValidator platoValidator;

    @Override
    public Plato crear(Plato plato, Long categoriaId) {

        platoValidator.validarCreacion(plato);

        CategoriaEntity categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la categoría con id: " + categoriaId
                ));

        PlatoEntity entity = platoPersistenceMapper.toEntity(plato);
        entity.setCategoria(categoria);

        PlatoEntity entityGuardada = platoRepository.save(entity);

        log.info("Plato creado correctamente con id: {}", entityGuardada.getId());

        return platoPersistenceMapper.toDomain(entityGuardada);
    }

    @Override
    public Plato obtenerPorId(Long id) {

        PlatoEntity entity = platoRepository.findById(id)
                .orElseThrow(() -> new PlatoNotFoundException(id));

        return platoPersistenceMapper.toDomain(entity);
    }

    @Override
    public List<Plato> obtenerTodos() {

        return platoRepository.findAll()
                .stream()
                .map(platoPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Plato> obtenerDisponibles() {

        return platoRepository.findByDisponibleTrue()
                .stream()
                .map(platoPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Plato actualizar(Long id, Plato plato, Long categoriaId) {

        PlatoEntity entityExistente = platoRepository.findById(id)
                .orElseThrow(() -> new PlatoNotFoundException(id));

        platoValidator.validarActualizacion(id, plato);

        CategoriaEntity categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la categoría con id: " + categoriaId
                ));

        entityExistente.setNombre(plato.getNombre());
        entityExistente.setDescripcion(plato.getDescripcion());
        entityExistente.setPrecio(plato.getPrecio());
        entityExistente.setDisponible(plato.getDisponible());
        entityExistente.setCategoria(categoria);

        PlatoEntity entityGuardada = platoRepository.save(entityExistente);

        log.info("Plato actualizado correctamente con id: {}", id);

        return platoPersistenceMapper.toDomain(entityGuardada);
    }

    @Override
    public void eliminar(Long id) {

        if (!platoRepository.existsById(id)) {
            throw new PlatoNotFoundException(id);
        }

        platoRepository.deleteById(id);

        log.info("Plato eliminado correctamente con id: {}", id);
    }
}