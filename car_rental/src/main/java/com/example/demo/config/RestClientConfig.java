package com.example.demo.config;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient reportRestClient(@Value("${report-service.url}") String reportServiceUrl) {

        return RestClient.builder()
                .baseUrl(reportServiceUrl)
                .requestInterceptor((request, body, execution) -> {
                    String requestId =MDC.get("requestId");

                    if (requestId != null) {
                        request.getHeaders().set(
                                "X-Request-ID",
                                requestId
                        );
                    }

                    return execution.execute(
                            request,
                            body
                    );
                })
                .build();
    }
}
