package com.restaurante.controller;

import com.restaurante.mapper.PlatoMapperIn;
import com.restaurante.mapper.PlatoMapperOut;
import com.restaurante.model.domain.Plato;
import com.restaurante.model.dto.request.PlatoRequestDTO;
import com.restaurante.model.dto.response.PlatoResponseDTO;
import com.restaurante.service.PlatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/platos")
@RequiredArgsConstructor
public class PlatoController {

    private final PlatoService platoService;
    private final PlatoMapperIn platoMapperIn;
    private final PlatoMapperOut platoMapperOut;

    @PostMapping
    public ResponseEntity<PlatoResponseDTO> crear(
            @Valid @RequestBody PlatoRequestDTO requestDTO) {

        Plato plato = platoMapperIn.toDomain(requestDTO);

        Plato creado = platoService.crear(
                plato,
                requestDTO.getCategoriaId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(platoMapperOut.toResponse(creado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatoResponseDTO> obtenerPorId(
            @PathVariable Long id) {

        Plato plato = platoService.obtenerPorId(id);

        return ResponseEntity.ok(
                platoMapperOut.toResponse(plato)
        );
    }

    @GetMapping
    public ResponseEntity<List<PlatoResponseDTO>> obtenerTodos() {

        List<PlatoResponseDTO> platos = platoService.obtenerTodos()
                .stream()
                .map(platoMapperOut::toResponse)
                .toList();

        return ResponseEntity.ok(platos);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<PlatoResponseDTO>> obtenerDisponibles() {

        List<PlatoResponseDTO> platos = platoService.obtenerDisponibles()
                .stream()
                .map(platoMapperOut::toResponse)
                .toList();

        return ResponseEntity.ok(platos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PlatoRequestDTO requestDTO) {

        Plato plato = platoMapperIn.toDomain(requestDTO);

        Plato actualizado = platoService.actualizar(
                id,
                plato,
                requestDTO.getCategoriaId()
        );

        return ResponseEntity.ok(
                platoMapperOut.toResponse(actualizado)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        platoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}