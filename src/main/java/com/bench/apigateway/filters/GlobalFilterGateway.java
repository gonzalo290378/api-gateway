package com.bench.apigateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class GlobalFilterGateway implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(GlobalFilterGateway.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("PRE Filter: Request");
        exchange.getRequest().mutate().headers(header ->
                header.add("token", "123456"));

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info("POST Filter: Response");

            Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token"))
                    .ifPresent(token -> {
                        exchange.getResponse().getHeaders()
                                .add("token", token);
                    });

            exchange.getResponse().getCookies()
                    .add("color", ResponseCookie.from("color", "red").build());

            //exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        }));
    }

    @Override
    public int getOrder() {
        return 10;
    }

}
