package com.example.demo.repository;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.CarStatusCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarReportRepository extends JpaRepository<CarEntity, Long> {

    @Query("""
        SELECT c.status AS status, COUNT(c) AS count FROM CarEntity c
        GROUP BY c.status
    """)
    List<CarStatusCount> countGroupByStatus();
}
