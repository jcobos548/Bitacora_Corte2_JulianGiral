package com.restaurante.persistence.repository;

import com.restaurante.persistence.entity.PlatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatoRepository extends JpaRepository<PlatoEntity, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    List<PlatoEntity> findByDisponibleTrue();
}