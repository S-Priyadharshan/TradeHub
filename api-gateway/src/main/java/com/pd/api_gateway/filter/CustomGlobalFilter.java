package com.pd.api_gateway.filter;

import com.pd.api_gateway.domain.dto.JwtPrincipal;
import com.pd.api_gateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CustomGlobalFilter implements GlobalFilter {

    private final JwtService jwtService;

    List<String> publicPaths = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/signup",
            "/api/v1/auth/refresh",
            "/api/v1/auth/keycloak/exchange");

    @Value("${HEADER_SECRET}")
    private String internalSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if(publicPaths.contains(exchange.getRequest().getURI().getPath())){
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String jwtToken = authHeader.substring(7);

            try{
                JwtPrincipal principal = jwtService.validateAndExtractClaims(jwtToken);

                // make sure to remove/sanitize any client supplied headers
                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .headers(h->{
                            h.remove("X-User-Id");
                            h.remove("X-User-Role");
                            h.remove("X-User-Name");
                            h.remove("X-User-Provider");
                            h.remove("X-Internal-Gateway");
                            h.remove("X-Request-Token");
                            h.remove("X-Account-Status");
                        })
                        .header("X-Internal-Gateway",internalSecret)
                        .header("X-Request-Token",jwtToken)
                        .header("X-User-Id",principal.userId())
                        .header("X-User-Name",principal.username())
                        .header("X-User-Role",principal.role())
                        .header("X-User-Provider",principal.provider())
                        .header("X-Account-Status",principal.accountStatus())
                        .build();

                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(modifiedRequest)
                        .build();

                return chain.filter(mutatedExchange);
            }catch(Exception e){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
