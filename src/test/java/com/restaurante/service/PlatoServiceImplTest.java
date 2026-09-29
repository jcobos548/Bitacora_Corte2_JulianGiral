package com.restaurante.service;

import com.restaurante.exception.PlatoConflictException;
import com.restaurante.exception.PlatoNotFoundException;
import com.restaurante.mapper.PlatoPersistenceMapper;
import com.restaurante.model.domain.Plato;
import com.restaurante.persistence.entity.PlatoEntity;
import com.restaurante.persistence.repository.PlatoRepository;
import com.restaurante.service.IMPL.PlatoServiceImpl;
import com.restaurante.validator.PlatoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoServiceImplTest {

    @Mock
    private PlatoRepository platoRepository;

    @Mock
    private PlatoPersistenceMapper platoPersistenceMapper;

    @Mock
    private PlatoValidator platoValidator;

    @InjectMocks
    private PlatoServiceImpl platoService;


    // =========================================================
    // CREAR
    // =========================================================

    @Test
    void crearPlatoValido() {

        // Arrange
        Plato plato = new Plato(
                null,
                "Bandeja Paisa",
                "Plato tradicional colombiano",
                28000.0,
                true
        );

        PlatoEntity entity = new PlatoEntity(
                1L,
                "Bandeja Paisa",
                "Plato tradicional colombiano",
                28000.0,
                true
        );

        Plato platoGuardado = new Plato(
                1L,
                "Bandeja Paisa",
                "Plato tradicional colombiano",
                28000.0,
                true
        );

        when(platoPersistenceMapper.toEntity(plato))
                .thenReturn(entity);

        when(platoRepository.save(entity))
                .thenReturn(entity);

        when(platoPersistenceMapper.toDomain(entity))
                .thenReturn(platoGuardado);

        // Act
        Plato resultado = platoService.crear(plato);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Bandeja Paisa", resultado.getNombre());

        verify(platoValidator).validarCreacion(plato);
        verify(platoRepository).save(entity);
        verify(platoPersistenceMapper).toEntity(plato);
        verify(platoPersistenceMapper).toDomain(entity);
    }


    // =========================================================
    // CREAR - DUPLICADO
    // =========================================================

    @Test
    void crearPlatoDuplicado() {

        // Arrange
        Plato plato = new Plato(
                null,
                "Bandeja Paisa",
                "Plato tradicional colombiano",
                28000.0,
                true
        );

        doThrow(new PlatoConflictException(
                "Ya existe un plato con el nombre: Bandeja Paisa"
        )).when(platoValidator).validarCreacion(plato);

        // Act + Assert
        assertThrows(
                PlatoConflictException.class,
                () -> platoService.crear(plato)
        );

        verify(platoValidator).validarCreacion(plato);
        verify(platoRepository, never()).save(any());
    }


    // =========================================================
    // OBTENER POR ID
    // =========================================================

    @Test
    void obtenerPorIdExistente() {

        // Arrange
        Long id = 1L;

        PlatoEntity entity = new PlatoEntity(
                1L,
                "Bandeja Paisa",
                "Plato tradicional colombiano",
                28000.0,
                true
        );

        Plato plato = new Plato(
                1L,
                "Bandeja Paisa",
                "Plato tradicional colombiano",
                28000.0,
                true
        );

        when(platoRepository.findById(id))
                .thenReturn(Optional.of(entity));

        when(platoPersistenceMapper.toDomain(entity))
                .thenReturn(plato);

        // Act
        Plato resultado = platoService.obtenerPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Bandeja Paisa", resultado.getNombre());

        verify(platoRepository).findById(id);
        verify(platoPersistenceMapper).toDomain(entity);
    }


    // =========================================================
    // OBTENER POR ID - NO ENCONTRADO
    // =========================================================

    @Test
    void obtenerPorIdNoExistente() {

        // Arrange
        Long id = 99L;

        when(platoRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                PlatoNotFoundException.class,
                () -> platoService.obtenerPorId(id)
        );

        verify(platoRepository).findById(id);
        verify(platoPersistenceMapper, never()).toDomain(any());
    }


    // =========================================================
    // OBTENER TODOS
    // =========================================================

    @Test
    void obtenerTodos() {

        // Arrange
        PlatoEntity entity1 = new PlatoEntity(
                1L,
                "Bandeja Paisa",
                "Plato tradicional colombiano",
                28000.0,
                true
        );

        PlatoEntity entity2 = new PlatoEntity(
                2L,
                "Tacos",
                "Tacos mexicanos",
                18000.0,
                true
        );

        Plato plato1 = new Plato(
                1L,
                "Bandeja Paisa",
                "Plato tradicional colombiano",
                28000.0,
                true
        );

        Plato plato2 = new Plato(
                2L,
                "Tacos",
                "Tacos mexicanos",
                18000.0,
                true
        );

        when(platoRepository.findAll())
                .thenReturn(List.of(entity1, entity2));

        when(platoPersistenceMapper.toDomain(entity1))
                .thenReturn(plato1);

        when(platoPersistenceMapper.toDomain(entity2))
                .thenReturn(plato2);

        // Act
        List<Plato> resultado = platoService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Bandeja Paisa", resultado.get(0).getNombre());
        assertEquals("Tacos", resultado.get(1).getNombre());

        verify(platoRepository).findAll();
    }


    // =========================================================
    // OBTENER TODOS - LISTA VACÍA
    // =========================================================

    @Test
    void obtenerTodosListaVacia() {

        // Arrange
        when(platoRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<Plato> resultado = platoService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(platoRepository).findAll();
    }


    // =========================================================
    // OBTENER DISPONIBLES
    // =========================================================

    @Test
    void obtenerDisponibles() {

        // Arrange
        PlatoEntity entity = new PlatoEntity(
                1L,
                "Tacos",
                "Tacos mexicanos",
                18000.0,
                true
        );

        Plato plato = new Plato(
                1L,
                "Tacos",
                "Tacos mexicanos",
                18000.0,
                true
        );

        when(platoRepository.findByDisponibleTrue())
                .thenReturn(List.of(entity));

        when(platoPersistenceMapper.toDomain(entity))
                .thenReturn(plato);

        // Act
        List<Plato> resultado = platoService.obtenerDisponibles();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getDisponible());

        verify(platoRepository).findByDisponibleTrue();
    }


    // =========================================================
    // ACTUALIZAR
    // =========================================================

    @Test
    void actualizarPlatoExistente() {

        // Arrange
        Long id = 1L;

        Plato platoActualizado = new Plato(
                null,
                "Tacos Especiales",
                "Tacos mexicanos especiales",
                22000.0,
                true
        );

        PlatoEntity entityExistente = new PlatoEntity(
                1L,
                "Tacos",
                "Tacos mexicanos",
                18000.0,
                true
        );

        PlatoEntity entityActualizada = new PlatoEntity(
                null,
                "Tacos Especiales",
                "Tacos mexicanos especiales",
                22000.0,
                true
        );

        Plato resultadoEsperado = new Plato(
                1L,
                "Tacos Especiales",
                "Tacos mexicanos especiales",
                22000.0,
                true
        );

        when(platoRepository.findById(id))
                .thenReturn(Optional.of(entityExistente));

        when(platoPersistenceMapper.toEntity(platoActualizado))
                .thenReturn(entityActualizada);

        when(platoRepository.save(entityActualizada))
                .thenReturn(entityActualizada);

        when(platoPersistenceMapper.toDomain(entityActualizada))
                .thenReturn(resultadoEsperado);

        // Act
        Plato resultado = platoService.actualizar(id, platoActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Tacos Especiales", resultado.getNombre());
        assertEquals(22000.0, resultado.getPrecio());

        assertEquals(1L, entityActualizada.getId());

        verify(platoRepository).findById(id);
        verify(platoValidator).validarActualizacion(id, platoActualizado);
        verify(platoRepository).save(entityActualizada);
    }


    // =========================================================
    // ACTUALIZAR - NO ENCONTRADO
    // =========================================================

    @Test
    void actualizarPlatoNoExistente() {

        // Arrange
        Long id = 99L;

        Plato plato = new Plato(
                null,
                "Tacos",
                "Tacos mexicanos",
                18000.0,
                true
        );

        when(platoRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                PlatoNotFoundException.class,
                () -> platoService.actualizar(id, plato)
        );

        verify(platoRepository).findById(id);
        verify(platoValidator, never()).validarActualizacion(anyLong(), any());
        verify(platoRepository, never()).save(any());
    }


    // =========================================================
    // ACTUALIZAR - DUPLICADO
    // =========================================================

    @Test
    void actualizarPlatoConNombreDuplicado() {

        // Arrange
        Long id = 1L;

        Plato plato = new Plato(
                null,
                "Bandeja Paisa",
                "Plato tradicional colombiano",
                28000.0,
                true
        );

        PlatoEntity entityExistente = new PlatoEntity(
                1L,
                "Tacos",
                "Tacos mexicanos",
                18000.0,
                true
        );

        when(platoRepository.findById(id))
                .thenReturn(Optional.of(entityExistente));

        doThrow(new PlatoConflictException(
                "Ya existe otro plato con el nombre: Bandeja Paisa"
        )).when(platoValidator)
                .validarActualizacion(id, plato);

        // Act + Assert
        assertThrows(
                PlatoConflictException.class,
                () -> platoService.actualizar(id, plato)
        );

        verify(platoRepository).findById(id);
        verify(platoValidator).validarActualizacion(id, plato);
        verify(platoRepository, never()).save(any());
    }


    // =========================================================
    // ELIMINAR
    // =========================================================

    @Test
    void eliminarPlatoExistente() {

        // Arrange
        Long id = 1L;

        when(platoRepository.existsById(id))
                .thenReturn(true);

        // Act
        assertDoesNotThrow(() -> platoService.eliminar(id));

        // Assert
        verify(platoRepository).existsById(id);
        verify(platoRepository).deleteById(id);
    }


    // =========================================================
    // ELIMINAR - NO ENCONTRADO
    // =========================================================

    @Test
    void eliminarPlatoNoExistente() {

        // Arrange
        Long id = 99L;

        when(platoRepository.existsById(id))
                .thenReturn(false);

        // Act + Assert
        assertThrows(
                PlatoNotFoundException.class,
                () -> platoService.eliminar(id)
        );

        verify(platoRepository).existsById(id);
        verify(platoRepository, never()).deleteById(anyLong());
    }
}