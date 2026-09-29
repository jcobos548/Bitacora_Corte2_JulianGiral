package com.restaurante.mapper;

import com.restaurante.model.domain.Categoria;
import com.restaurante.persistence.entity.CategoriaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaPersistenceMapper {

    CategoriaEntity toEntity(Categoria categoria);

    Categoria toDomain(CategoriaEntity entity);
}