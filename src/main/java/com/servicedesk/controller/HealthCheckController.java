package com.servicedesk.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Health check REST Controller to verify application availability and status.
 */
@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health Check", description = "Endpoints for application status and uptime verification")
public class HealthCheckController {

    @GetMapping
    @Operation(summary = "Check application health", description = "Returns service availability status and current timestamp.")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("service", "IT Service Desk Management System");
        response.put("message", "Application is healthy and running smoothly.");
        response.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(response);
    }
}
