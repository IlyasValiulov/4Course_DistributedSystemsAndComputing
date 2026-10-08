package com.example.demo.services;

import com.example.demo.dto.report.CarReportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ReportClient {

    private static final Logger log = LoggerFactory.getLogger(ReportClient.class);

    private final RestClient reportRestClient;

    public ReportClient(@Qualifier("reportRestClient") RestClient reportRestClient) {
        this.reportRestClient = reportRestClient;
    }

    public CarReportResponse getReport() {
        log.info("Requesting report from report-service");

        CarReportResponse response =
                reportRestClient
                        .get()
                        .uri("/api/reports/status")
                        .retrieve()
                        .body(CarReportResponse.class);

        log.info(
                "Report received: inSalon={}, inRent={}, writtenOff={}",
                response != null ? response.getInSalon() : null,
                response != null ? response.getInRent() : null,
                response != null ? response.getWrittenOff() : null
        );

        return response;
    }
}
