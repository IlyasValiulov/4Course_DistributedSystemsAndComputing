
package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.client.RestClient;

import java.util.concurrent.Executor;

@Configuration
public class RestClientConfig {

    private RestClient createRestClient(String baseUrl) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor((request, body, execution) -> {

                
                    String requestId = MDC.get("requestId");

                    if (requestId != null) {
                        request.getHeaders().set(
                                "X-Request-ID",
                                requestId
                        );
                    }

                
                    if (RequestContextHolder.getRequestAttributes()
                            instanceof ServletRequestAttributes attributes) {

                        HttpServletRequest servletRequest =
                                attributes.getRequest();

                        String authorization =
                                servletRequest.getHeader(
                                        HttpHeaders.AUTHORIZATION
                                );

                        if (authorization != null
                                && !authorization.isBlank()) {

                            request.getHeaders().set(
                                    HttpHeaders.AUTHORIZATION,
                                    authorization
                            );
                        }
                    }

                    return execution.execute(request, body);
                })
                .build();
    }

    @Bean
    public RestClient reportRestClient(
            @Value("${report-service.url}") String reportServiceUrl) {

        return createRestClient(reportServiceUrl);
    }

    @Bean
    public RestClient fileStorageRestClient(
            @Value("${file-storage-service.url}") String fileStorageUrl) {

        return createRestClient(fileStorageUrl);
    }

    @Bean(name = "fileStorageExecutor")
    public Executor fileStorageExecutor() {
        ThreadPoolTaskExecutor executor =new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("file-storage-async-");

        executor.initialize();

        return executor;
    }
}
