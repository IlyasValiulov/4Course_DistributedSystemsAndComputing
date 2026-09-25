package com.example.demo.controllers;

import com.example.demo.dto.car.CarRequest;
import com.example.demo.dto.car.CarResponse;
import com.example.demo.dto.report.CarReportResponse;
import com.example.demo.services.CarService;
import com.example.demo.services.ReportClient;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    private final ReportClient reportClient;

    public CarController(CarService carService, ReportClient reportClient) {
        this.carService = carService;
        this.reportClient = reportClient;
    }

    @GetMapping
    public List<CarResponse> getAll() {
        return carService.getAll();
    }

    @GetMapping("/{id}")
    public CarResponse getById(@PathVariable Long id) {
        return carService.getById(id);
    }

    @PostMapping
    public CarResponse create(@Valid @RequestBody CarRequest dto) {
        return carService.create(dto);
    }

    @PutMapping("/{id}")
    public CarResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CarRequest dto) {
        return carService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        carService.delete(id);
    }

    @PostMapping("/{id}/for-rent")
    public CarResponse forRent(@PathVariable Long id) {
        return carService.forRent(id);
    }

    @PostMapping("/{id}/from-rent")
    public CarResponse fromRent(@PathVariable Long id) {
        return carService.fromRent(id);
    }

    @PostMapping("/{id}/write-off")
    public CarResponse writeOff(@PathVariable Long id) {
        return carService.writeOff(id);
    }

    @GetMapping("/deleted")
    public List<CarResponse> getAllDeleted() {
        return carService.getAllDeleted();
    }

    @GetMapping("/deleted/{id}")
    public CarResponse getDeletedById(@PathVariable Long id) {
        return carService.getDeletedById(id);
    }

    @GetMapping("/report")
    public CarReportResponse getReportStatus() {
        return reportClient.getReportStatus();
    }
}
