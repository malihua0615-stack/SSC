package com.example.gateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import com.example.common.util.JwtUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoginFilter implements GlobalFilter, Ordered {

    private final String[] PATH = {
            "/auth/login",
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String token = getToken(exchange);
        if (Arrays.asList(PATH).contains(path)){
            return chain.filter(exchange);
        }
        if (token == null ) {
            log.warn("token is null");
            return unauthorizedResponse(exchange,"先登录一下！");
        }
        String id = JwtUtil.getId(token);
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("SSC-User-Id", id)
                .build();
        log.debug("Token 校验通过。: userId={}, path={}", id, path);
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return -100;
    }


    private String getToken(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String a = request.getHeaders().getFirst("Authorization");
        if (a != null && a.startsWith("Bearer ")) {
            return a.substring(7);
        }
        return null;
    }

    /**
     * 返回 401
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"code\":401,\"message\":\"%s\"}", message);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
