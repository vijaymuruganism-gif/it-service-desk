package com.servicedesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the IT Service Desk Management System.
 *
 * <p>The {@link SpringBootApplication} annotation enables:
 * <ul>
 *   <li>{@code @Configuration}: Marks the class as a source of bean definitions.</li>
 *   <li>{@code @EnableAutoConfiguration}: Directs Spring Boot to automatically configure
 *       beans based on classpath dependencies (e.g., Spring MVC, Hibernate/JPA).</li>
 *   <li>{@code @ComponentScan}: Scans for Spring components (@RestController, @Service,
 *       @Repository, etc.) within {@code com.servicedesk} and its subpackages.</li>
 * </ul>
 */
@SpringBootApplication
public class ServiceDeskApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDeskApplication.class, args);
    }
}
