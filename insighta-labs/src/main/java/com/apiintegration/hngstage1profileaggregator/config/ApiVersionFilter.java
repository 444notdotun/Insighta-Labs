package com.apiintegration.hngstage1profileaggregator.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class ApiVersionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (path.startsWith("/api/") && !method.equalsIgnoreCase("OPTIONS")) {
            String version = request.getHeader("X-API-Version");
            if (version == null || !version.equals("1")) {
                response.setStatus(400);
                response.setContentType("application/json");
                response.getWriter().write("{\"status\":\"error\",\"message\":\"API version header required\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}

