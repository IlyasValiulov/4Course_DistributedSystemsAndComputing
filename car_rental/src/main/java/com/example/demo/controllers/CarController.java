package com.example.demo.controllers;

import com.example.demo.dto.report.CarReportResponse;
import com.example.demo.dto.car.CarRequest;
import com.example.demo.dto.car.CarResponse;
import com.example.demo.services.CarService;
import com.example.demo.services.ReportClient;
import com.example.demo.services.CarReportExportService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private static final Logger log = LoggerFactory.getLogger(CarController.class);

    private final CarService carService;

    private final ReportClient reportClient;

    private final CarReportExportService carReportExportService;

    public CarController(
            CarService carService,
            ReportClient reportClient,
            CarReportExportService carReportExportService) {
        this.carService = carService;
        this.reportClient = reportClient;
        this.carReportExportService = carReportExportService;
    }

    @GetMapping
    public List<CarResponse> getAll() {
        log.info("Getting all cars");
        return carService.getAll();
    }

    @GetMapping("/{id}")
    public CarResponse getById(@PathVariable Long id) {
        log.info("Getting car by id={}", id);
        return carService.getById(id);
    }

    @PostMapping
    public CarResponse create(@Valid @RequestBody CarRequest dto) {
        log.info(
                "Creating car: brand={}, model={}, productionYear={}",
                dto.getBrand(),
                dto.getModel(),
                dto.getProductionYear()
        );
        return carService.create(dto);
    }

    @PutMapping("/{id}")
    public CarResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CarRequest dto) {
        log.info("Updating car id={}", id);
        return carService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Deleting car id={}", id);
        carService.delete(id);
    }

    @PostMapping("/{id}/for-rent")
    public CarResponse forRent(@PathVariable Long id) {
        log.info("Moving car id={} to rental", id);
        return carService.forRent(id);
    }

    @PostMapping("/{id}/from-rent")
    public CarResponse fromRent(@PathVariable Long id) {
        log.info("Returning car id={} from rental", id);
        return carService.fromRent(id);
    }

    @PostMapping("/{id}/write-off")
    public CarResponse writeOff(@PathVariable Long id) {
        log.info("Writing off car id={}", id);
        return carService.writeOff(id);
    }

    @GetMapping("/deleted")
    public List<CarResponse> getAllDeleted() {
        log.info("Getting all deleted cars");
        return carService.getAllDeleted();
    }

    @GetMapping("/deleted/{id}")
    public CarResponse getDeletedById(@PathVariable Long id) {
        log.info("Getting deleted car id={}", id);
        return carService.getDeletedById(id);
    }

    @GetMapping("/report")
    public CarReportResponse getReport() {
        log.info("Getting cars report");
        return reportClient.getReport();
    }

    @PostMapping("/report/export")
    public String exportReport() {
        log.info("Exporting car report to file storage");
        return carReportExportService.exportReport();
    }
}
