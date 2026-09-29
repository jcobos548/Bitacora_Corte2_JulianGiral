package com.restaurante.mapper;

import com.restaurante.model.domain.Plato;
import com.restaurante.model.dto.response.PlatoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlatoMapperOut {

    @Mapping(target = "categoriaId", source = "categoria.id")
    PlatoResponseDTO toResponse(Plato plato);
}