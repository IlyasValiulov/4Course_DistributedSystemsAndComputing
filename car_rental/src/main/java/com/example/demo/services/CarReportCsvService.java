package com.example.demo.services;

import com.example.demo.dto.report.CarReportResponse;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class CarReportCsvService {

    public byte[] createCsv(CarReportResponse report) {
        String csv =
                "status,count\n" +
                "SALON," +
                report.getInSalon() +
                "\n" +
                "RENTED," +
                report.getInRent() +
                "\n" +
                "WRITTEN_OFF," +
                report.getWrittenOff() +
                "\n";

        return csv.getBytes(StandardCharsets.UTF_8);
    }
}