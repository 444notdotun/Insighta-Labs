package com.apiintegration.hngstage1profileaggregator.data.repository;

import com.apiintegration.hngstage1profileaggregator.data.model.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLoggerRepository extends JpaRepository<RequestLog, Long> {
}
