package com.servicedesk.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Health check REST Controller to verify application availability and status.
 *
 * <p>Annotated with {@link RestController}, which combines {@code @Controller}
 * and {@code @ResponseBody}. It tells Spring to serialize the returned data
 * directly into the HTTP response body as JSON.
 */
@RestController
@RequestMapping("/api/v1/health")
public class HealthCheckController {

    /**
     * Basic health check endpoint.
     *
     * @return HTTP 200 with service status, application name, and current timestamp.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("service", "IT Service Desk Management System");
        response.put("message", "Application is healthy and running smoothly.");
        response.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(response);
    }
}
