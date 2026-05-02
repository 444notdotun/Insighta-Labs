package com.apiintegration.hngstage1profileaggregator.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
    public class RateLimitFilter extends OncePerRequestFilter {

        private final Map<String, List<Long>> requestCounts = new ConcurrentHashMap<>();
    private static final int AUTH_LIMIT = 100;
    private static final int API_LIMIT = 200;
        private static final long ONE_MINUTE = 60_000L;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            String ip = request.getRemoteAddr();
            String path = request.getRequestURI();
            int limit = path.startsWith("/auth") ? AUTH_LIMIT : API_LIMIT;
            requestCounts.putIfAbsent(ip, new ArrayList<>());
            List<Long> timestamps = requestCounts.get(ip);
            long now = System.currentTimeMillis();
            synchronized (timestamps) {
                timestamps.removeIf(t -> now - t > ONE_MINUTE);
                if (timestamps.size() >= limit) {
                    response.setStatus(429);
                    response.getWriter().write("{\"status\":\"error\",\"message\":\"Too many requests\"}");
                    return;
                }
                timestamps.add(now);
            }
            filterChain.doFilter(request, response);
        }
    }

