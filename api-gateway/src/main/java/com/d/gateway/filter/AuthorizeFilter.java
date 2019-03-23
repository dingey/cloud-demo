package com.d.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
//import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
//import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//import java.util.Set;

@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    private static final String AUTHORIZE_TOKEN = "token";
//    private final StringRedisTemplate srt;

//    public AuthorizeFilter(StringRedisTemplate srt) {
//        this.srt = srt;
//    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (request.getPath().subPath(2).value().startsWith("/open")||!request.getPath().subPath(2).value().startsWith("/auth")) {
            return chain.filter(exchange);
        }
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(AUTHORIZE_TOKEN);
        if (token == null) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }
        if (token == null) {
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            if (!cookies.isEmpty() && cookies.get("token") != null && !cookies.get("token").isEmpty()) {
                token = cookies.get("token").get(0).getValue();
            }
        }
        ServerHttpResponse response = exchange.getResponse();
        if (StringUtils.isEmpty(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
//        if (request.getPath().value().startsWith("/admin")) {
//            Set<String> set = srt.opsForSet().members(token);
//            String path = request.getPath().value();
//            PathMatcher matcher = new AntPathMatcher();
//            if (set != null) {
//                for (String grantPath : set) {
//                    if (matcher.match(grantPath, path)) {
//                        return chain.filter(exchange);
//                    }
//                }
//            }
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
//        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
