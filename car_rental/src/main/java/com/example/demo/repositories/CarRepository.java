package com.example.demo.repositories;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {

    List<CarEntity> findByStatus(CarStatus status);
}
