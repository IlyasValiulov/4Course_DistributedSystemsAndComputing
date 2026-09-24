package com.example.demo.repositories;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {

    List<CarEntity> findByStatus(CarStatus status);

    @Query(value = "SELECT * FROM cars WHERE deleted = true", nativeQuery = true)
    List<CarEntity> findAllDeleted();

    @Query(value = "SELECT * FROM cars WHERE id = :id AND deleted = true", nativeQuery = true)
    Optional<CarEntity> findDeletedById(@Param("id") Long id);
}
