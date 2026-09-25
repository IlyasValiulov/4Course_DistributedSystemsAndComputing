package com.example.demo.controller;

import com.example.demo.dto.CarReportResponse;
import com.example.demo.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/status")
    public CarReportResponse getCarsReport() {
        return reportService.getCarsReport();
    }
}
