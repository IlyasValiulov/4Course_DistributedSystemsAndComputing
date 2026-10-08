package com.example.demo.services;

import com.example.demo.dto.report.CarReportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CarReportExportService {

    private static final Logger log = LoggerFactory.getLogger(CarReportExportService.class);

    private final ReportClient reportClient;

    private final CarReportCsvService csvService;

    private final FileStorageClient fileStorageClient;

    public CarReportExportService(ReportClient reportClient, CarReportCsvService csvService, FileStorageClient fileStorageClient) {
        this.reportClient = reportClient;
        this.csvService = csvService;
        this.fileStorageClient = fileStorageClient;
    }

    public String exportReport() {
        log.info("Starting car report export");
        CarReportResponse report = reportClient.getReport();
        byte[] csv = csvService.createCsv(report);
        String filename = "cars-report-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".csv";
        fileStorageClient.upload(filename, csv);
        log.info("Car report exported successfully: filename={}", filename);
        return filename;
    }
}
