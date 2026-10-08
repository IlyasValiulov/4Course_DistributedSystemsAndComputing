package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class RequestIdGlobalFilter implements WebFilter, Ordered {

    private static final Logger log =
            LoggerFactory.getLogger(RequestIdGlobalFilter.class);

    private static final String REQUEST_ID_HEADER =
            "X-Request-ID";

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain) {

        String requestId =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(REQUEST_ID_HEADER);

        if (!StringUtils.hasText(requestId)) {
            requestId = UUID.randomUUID().toString();
        }

        final String finalRequestId = requestId;

        ServerHttpRequest mutatedRequest =
                exchange.getRequest()
                        .mutate()
                        .header(
                                REQUEST_ID_HEADER,
                                finalRequestId
                        )
                        .build();

        ServerWebExchange mutatedExchange =
                exchange.mutate()
                        .request(mutatedRequest)
                        .build();

        mutatedExchange.getResponse()
                .getHeaders()
                .set(
                        REQUEST_ID_HEADER,
                        finalRequestId
                );

        long start = System.currentTimeMillis();

        MDC.put("requestId", finalRequestId);

        log.info(
                "Gateway request started: method={}, uri={}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI()
        );

        MDC.remove("requestId");

        return chain.filter(mutatedExchange)

                .doOnSuccess(unused -> {

                    MDC.put(
                            "requestId",
                            finalRequestId
                    );

                    long duration =
                            System.currentTimeMillis() - start;

                    log.info(
                            "Gateway request completed: method={}, uri={}, status={}, durationMs={}",
                            exchange.getRequest().getMethod(),
                            exchange.getRequest().getURI(),
                            mutatedExchange.getResponse()
                                    .getStatusCode(),
                            duration
                    );

                    MDC.remove("requestId");
                })

                .doOnError(error -> {

                    MDC.put(
                            "requestId",
                            finalRequestId
                    );

                    long duration =
                            System.currentTimeMillis() - start;

                    log.error(
                            "Gateway request failed: method={}, uri={}, durationMs={}",
                            exchange.getRequest().getMethod(),
                            exchange.getRequest().getURI(),
                            duration,
                            error
                    );

                    MDC.remove("requestId");
                });
    }
}
