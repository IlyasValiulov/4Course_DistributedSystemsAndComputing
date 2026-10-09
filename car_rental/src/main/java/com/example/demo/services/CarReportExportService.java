package com.example.demo.services;

import com.example.demo.dto.report.CarReportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CarReportExportService {

    private static final Logger log =LoggerFactory.getLogger(CarReportExportService.class);
    private final ReportClient reportClient;
    private final CarReportCsvService csvService;
    private final FileStorageQueueProducer queueProducer;

    public CarReportExportService(
            ReportClient reportClient,
            CarReportCsvService csvService,
            FileStorageQueueProducer queueProducer) {
        this.reportClient = reportClient;
        this.csvService = csvService;
        this.queueProducer = queueProducer;
    }

    public String exportReport() {
        log.info("Starting car report export");
        CarReportResponse report = reportClient.getReport();

        log.info(
                "Creating CSV from report: inSalon={}, inRent={}, writtenOff={}",
                report.getInSalon(),
                report.getInRent(),
                report.getWrittenOff()
        );

        byte[] csv = csvService.createCsv(report);
        String filename =
                "cars-report-" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) +
                ".csv";
        log.info("CSV created: filename={}, size={}", filename, csv.length);
        queueProducer.send( filename, csv);
        log.info("Car report export request sent to file storage: filename={}", filename);
        return filename;
    }
}
