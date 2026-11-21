package com.servicedesk.config;

import com.servicedesk.entity.*;
import com.servicedesk.repository.CategoryRepository;
import com.servicedesk.repository.TicketRepository;
import com.servicedesk.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds initial baseline data (categories, users with BCrypt hashed passwords, sample ticket) into the database on startup.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(CategoryRepository categoryRepository,
                           UserRepository userRepository,
                           TicketRepository ticketRepository,
                           PasswordEncoder passwordEncoder) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.passwordEncoder = passwordEncoder;
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

        // 2. Seed initial users with encrypted passwords (admin123, engineer123, employee123)
        if (userRepository.count() == 0) {
            log.info("Seeding initial users with BCrypt password hashing...");
            User admin = new User("admin", "admin@servicedesk.com", passwordEncoder.encode("admin123"), Role.ADMIN, "IT Infrastructure");
            User engineer = new User("john_support", "john.support@servicedesk.com", passwordEncoder.encode("engineer123"), Role.SUPPORT_ENGINEER, "IT Service Desk");
            User employee = new User("alice_employee", "alice.employee@company.com", passwordEncoder.encode("employee123"), Role.EMPLOYEE, "Finance & Operations");

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
