package com.restaurante.mapper;

import com.restaurante.model.domain.Plato;
import com.restaurante.model.dto.request.PlatoRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlatoMapperIn {

    @Mapping(target = "id", ignore = true)
    Plato toDomain(PlatoRequestDTO requestDTO);
}