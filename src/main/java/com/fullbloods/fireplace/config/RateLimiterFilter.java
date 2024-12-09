package com.fullbloods.fireplace.config;

import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {

    private final Map<String, RateLimiter> rateLimiterMap = new HashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();  // 클라이언트 IP 주소 추출
        RateLimiter rateLimiter = rateLimiterMap.computeIfAbsent(clientIp, ip -> RateLimiter.create(5));

        if (rateLimiter.tryAcquire()) {
            filterChain.doFilter(request, response);  // 요청 허용
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 429 상태 코드
            response.getWriter().write("Rate limit exceeded for IP: " + clientIp);
        }
    }
}
