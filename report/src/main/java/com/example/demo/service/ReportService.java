package com.example.demo.service;

import com.example.demo.dto.CarReportResponse;
import com.example.demo.entities.CarStatus;
import com.example.demo.entities.CarStatusCount;
import com.example.demo.repository.CarReportRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final CarReportRepository repository;

    public ReportService(CarReportRepository repository) {
        this.repository = repository;
    }

    public CarReportResponse getCarsReport() {
        Map<CarStatus, Long> counts = repository.countGroupByStatus().stream()
                .collect(Collectors.toMap(CarStatusCount::getStatus, CarStatusCount::getCount));

        long inRent = counts.getOrDefault(CarStatus.RENTED, 0L);
        long writtenOff = counts.getOrDefault(CarStatus.WRITTEN_OFF, 0L);
        long inSalon = counts.getOrDefault(CarStatus.SALON, 0L);

        return new CarReportResponse(inSalon, inRent, writtenOff);
    }
}
