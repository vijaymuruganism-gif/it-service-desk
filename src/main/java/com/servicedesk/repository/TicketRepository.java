package com.servicedesk.repository;

import com.servicedesk.entity.Priority;
import com.servicedesk.entity.Ticket;
import com.servicedesk.entity.TicketStatus;
import com.servicedesk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for {@link Ticket} entity.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Finds all tickets currently at a specific lifecycle stage (e.g. OPEN, RESOLVED).
     */
    List<Ticket> findByStatus(TicketStatus status);

    /**
     * Finds all tickets by priority level (e.g. CRITICAL, HIGH).
     */
    List<Ticket> findByPriority(Priority priority);

    /**
     * Finds all tickets raised by a specific employee.
     */
    List<Ticket> findByCreatedBy(User createdBy);

    /**
     * Finds all tickets assigned to a specific IT support engineer.
     */
    List<Ticket> findByAssignedTo(User assignedTo);

    /**
     * Finds all tickets grouped under a specific category id.
     */
    List<Ticket> findByCategoryId(Long categoryId);

    /**
     * Counts how many tickets currently exist with a given status (useful for dashboard metrics).
     */
    long countByStatus(TicketStatus status);
}
