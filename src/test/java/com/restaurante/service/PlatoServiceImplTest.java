package com.restaurante.service;

import com.restaurante.exception.PlatoConflictException;
import com.restaurante.exception.PlatoNotFoundException;
import com.restaurante.model.domain.Plato;
import com.restaurante.service.IMPL.PlatoServiceImpl;
import com.restaurante.validator.PlatoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoServiceImplTest {

    @Mock
    private PlatoValidator platoValidator;

    private PlatoServiceImpl platoService;

    @BeforeEach
    void setUp() {
        platoService = new PlatoServiceImpl(platoValidator);
    }

    @Test
    void deberiaCrearPlatoCorrectamente() {

        Plato plato = new Plato(
                null,
                "Taco al Pastor",
                "Taco tradicional mexicano",
                12000.0,
                true
        );

        Plato resultado = platoService.crear(plato);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Taco al Pastor", resultado.getNombre());
        assertEquals(12000.0, resultado.getPrecio());
        assertTrue(resultado.getDisponible());

        verify(platoValidator).validarCreacion(
                eq(plato),
                anyCollection()
        );
    }

    @Test
    void deberiaObtenerPlatoPorId() {

        Plato plato = new Plato(
                null,
                "Burrito",
                "Burrito mexicano",
                18000.0,
                true
        );

        Plato creado = platoService.crear(plato);

        Plato resultado = platoService.obtenerPorId(creado.getId());

        assertNotNull(resultado);
        assertEquals(creado.getId(), resultado.getId());
        assertEquals("Burrito", resultado.getNombre());
    }

    @Test
    void deberiaLanzarExcepcionCuandoPlatoNoExiste() {

        assertThrows(
                PlatoNotFoundException.class,
                () -> platoService.obtenerPorId(999L)
        );
    }

    @Test
    void deberiaLanzarConflictoCuandoNombreEstaDuplicado() {

        Plato plato = new Plato(
                null,
                "Quesadilla",
                "Quesadilla mexicana",
                10000.0,
                true
        );

        doThrow(new PlatoConflictException(
                "Ya existe un plato con el nombre: Quesadilla"
        )).when(platoValidator)
                .validarCreacion(
                        eq(plato),
                        anyCollection()
                );

        assertThrows(
                PlatoConflictException.class,
                () -> platoService.crear(plato)
        );

        verify(platoValidator).validarCreacion(
                eq(plato),
                anyCollection()
        );
    }

    @Test
    void deberiaObtenerSoloPlatosDisponibles() {

        Plato platoDisponible = new Plato(
                null,
                "Taco de Carnitas",
                "Taco con carne de carnitas",
                14000.0,
                true
        );

        Plato platoNoDisponible = new Plato(
                null,
                "Enchiladas",
                "Enchiladas mexicanas",
                16000.0,
                false
        );

        platoService.crear(platoDisponible);
        platoService.crear(platoNoDisponible);

        List<Plato> disponibles =
                platoService.obtenerDisponibles();

        assertEquals(1, disponibles.size());
        assertEquals(
                "Taco de Carnitas",
                disponibles.get(0).getNombre()
        );
        assertTrue(disponibles.get(0).getDisponible());
    }

    @Test
    void deberiaActualizarPlatoCorrectamente() {

        Plato platoOriginal = new Plato(
                null,
                "Taco",
                "Taco mexicano",
                10000.0,
                true
        );

        Plato creado = platoService.crear(platoOriginal);

        Plato platoActualizado = new Plato(
                null,
                "Taco de Carnitas",
                "Taco con carne de carnitas",
                14000.0,
                true
        );

        Plato resultado = platoService.actualizar(
                creado.getId(),
                platoActualizado
        );

        assertEquals(creado.getId(), resultado.getId());
        assertEquals(
                "Taco de Carnitas",
                resultado.getNombre()
        );
        assertEquals(14000.0, resultado.getPrecio());

        verify(platoValidator).validarActualizacion(
                eq(creado.getId()),
                eq(platoActualizado),
                anyCollection()
        );
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarPlatoInexistente() {

        Plato plato = new Plato(
                null,
                "Taco",
                "Taco mexicano",
                10000.0,
                true
        );

        assertThrows(
                PlatoNotFoundException.class,
                () -> platoService.actualizar(999L, plato)
        );

        verify(
                platoValidator,
                never()
        ).validarActualizacion(
                anyLong(),
                any(Plato.class),
                anyCollection()
        );
    }

    @Test
    void deberiaEliminarPlatoCorrectamente() {

        Plato plato = new Plato(
                null,
                "Tostadas",
                "Tostadas mexicanas",
                9000.0,
                true
        );

        Plato creado = platoService.crear(plato);

        platoService.eliminar(creado.getId());

        assertThrows(
                PlatoNotFoundException.class,
                () -> platoService.obtenerPorId(creado.getId())
        );
    }

    @Test
    void deberiaLanzarExcepcionAlEliminarPlatoInexistente() {

        assertThrows(
                PlatoNotFoundException.class,
                () -> platoService.eliminar(999L)
        );
    }
}