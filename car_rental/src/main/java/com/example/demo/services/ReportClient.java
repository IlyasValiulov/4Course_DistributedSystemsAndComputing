package com.example.demo.services;

import com.example.demo.dto.report.CarReportResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ReportClient {

    private final RestClient reportRestClient;

    private final String reportUrl = "/api/reports";

    public ReportClient(RestClient reportRestClient) {
        this.reportRestClient = reportRestClient;
    }

    public CarReportResponse getReportStatus() {
        return reportRestClient
                .get()
                .uri(reportUrl + "/status")
                .retrieve()
                .body(CarReportResponse.class);
    }
}
