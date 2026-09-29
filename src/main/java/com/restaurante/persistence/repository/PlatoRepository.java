package com.restaurante.persistence.repository;

import com.restaurante.persistence.entity.PlatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlatoRepository extends JpaRepository<PlatoEntity, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    Optional<PlatoEntity> findByNombreIgnoreCase(String nombre);

    List<PlatoEntity> findByDisponibleTrue();
}