package com.servicedesk.config;

import com.servicedesk.entity.*;
import com.servicedesk.repository.CategoryRepository;
import com.servicedesk.repository.TicketRepository;
import com.servicedesk.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds initial baseline data (categories, users, sample ticket) into the database on startup.
 *
 * <p>Implements {@link CommandLineRunner}, whose {@code run} method is invoked by
 * Spring Boot immediately after the application context has fully initialized.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public DataInitializer(CategoryRepository categoryRepository,
                           UserRepository userRepository,
                           TicketRepository ticketRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void run(String... args) {
        log.info("Checking database baseline seed data...");

        // 1. Seed default support categories
        if (categoryRepository.count() == 0) {
            log.info("Seeding initial support categories...");
            categoryRepository.saveAll(List.of(
                    new Category("Hardware", "Physical computing equipment: laptops, monitors, peripherals"),
                    new Category("Software", "Operating systems, office suites, engineering tools, and license requests"),
                    new Category("Network & Connectivity", "Wi-Fi access, VPN issues, DNS resolution, and firewall rules"),
                    new Category("Access & Identity", "Account permissions, password resets, and Single Sign-On (SSO)")
            ));
        }

        // 2. Seed initial users for testing (Admin, Support Engineer, Employee)
        if (userRepository.count() == 0) {
            log.info("Seeding initial users...");
            User admin = new User("admin", "admin@servicedesk.com", "admin123", Role.ADMIN, "IT Infrastructure");
            User engineer = new User("john_support", "john.support@servicedesk.com", "engineer123", Role.SUPPORT_ENGINEER, "IT Service Desk");
            User employee = new User("alice_employee", "alice.employee@company.com", "employee123", Role.EMPLOYEE, "Finance & Operations");

            userRepository.saveAll(List.of(admin, engineer, employee));

            // 3. Seed an initial demonstration ticket
            Category hardwareCategory = categoryRepository.findByName("Hardware").orElse(null);
            if (hardwareCategory != null) {
                Ticket sampleTicket = new Ticket(
                        "Laptop Screen Flickering",
                        "The primary laptop display flickers intermittently when plugged into the docking station.",
                        Priority.HIGH,
                        hardwareCategory,
                        employee
                );
                sampleTicket.setAssignedTo(engineer);
                sampleTicket.setStatus(TicketStatus.IN_PROGRESS);
                ticketRepository.save(sampleTicket);
                log.info("Sample ticket seeded successfully with ID: {}", sampleTicket.getId());
            }
        }

        log.info("Database baseline check complete. Ready to serve requests!");
    }
}
