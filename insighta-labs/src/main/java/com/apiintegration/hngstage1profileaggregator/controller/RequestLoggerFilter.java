package com.apiintegration.hngstage1profileaggregator.controller;

import com.apiintegration.hngstage1profileaggregator.data.model.RequestLog;
import com.apiintegration.hngstage1profileaggregator.data.repository.RequestLoggerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.LocalDateTime;


@Component
public class RequestLoggerFilter extends OncePerRequestFilter {
    @Autowired
    private RequestLoggerRepository requestLoggerRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(request, wrappedResponse);
        } finally {
            long responseTime = System.currentTimeMillis() - start;
            try {
                RequestLog log = new RequestLog();
                log.setMethod(request.getMethod());
                log.setEndpoint(request.getRequestURI());
                log.setStatus(wrappedResponse.getStatus());
                log.setResponseTime(responseTime);
                log.setTimestamp(LocalDateTime.now());
                requestLoggerRepository.save(log);
            } catch (Exception ignored) {}
            wrappedResponse.copyBodyToResponse();
        }
    }
}
