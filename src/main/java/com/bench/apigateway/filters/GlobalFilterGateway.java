package com.bench.apigateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class GlobalFilterGateway implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(GlobalFilterGateway.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Pre Filter: Request");
        //exchange.getRequest().mutate().headers(h -> h.add("token", "123456"));

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info("Post Filter: Response");

//            Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(valor -> {
//                exchange.getResponse().getHeaders().add("token", valor);
//            });

            exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "red").build());
        }));
    }

    @Override
    public int getOrder() {
        return 10;
    }

}
