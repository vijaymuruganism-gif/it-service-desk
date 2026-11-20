package com.servicedesk.service;

import com.servicedesk.dto.TicketRequestDto;
import com.servicedesk.dto.TicketResponseDto;
import com.servicedesk.entity.Priority;
import com.servicedesk.entity.TicketStatus;

import java.util.List;

/**
 * Service contract defining business operations for ticket management.
 */
public interface TicketService {

    /**
     * Raises a new support ticket in the system.
     */
    TicketResponseDto createTicket(TicketRequestDto request);

    /**
     * Retrieves all support tickets.
     */
    List<TicketResponseDto> getAllTickets();

    /**
     * Retrieves a single ticket by its ID.
     */
    TicketResponseDto getTicketById(Long id);

    /**
     * Filters tickets by their lifecycle status (e.g. OPEN, IN_PROGRESS).
     */
    List<TicketResponseDto> getTicketsByStatus(TicketStatus status);

    /**
     * Filters tickets by priority level.
     */
    List<TicketResponseDto> getTicketsByPriority(Priority priority);

    /**
     * Retrieves all tickets raised by a specific employee.
     */
    List<TicketResponseDto> getTicketsByEmployee(Long employeeId);

    /**
     * Retrieves all tickets assigned to a specific engineer.
     */
    List<TicketResponseDto> getTicketsByEngineer(Long engineerId);

    /**
     * Assigns a ticket to an IT support engineer and transitions status to ASSIGNED.
     */
    TicketResponseDto assignTicket(Long ticketId, Long engineerId);

    /**
     * Updates the lifecycle status of a ticket (e.g. IN_PROGRESS, RESOLVED, CLOSED).
     */
    TicketResponseDto updateTicketStatus(Long ticketId, TicketStatus newStatus);

    /**
     * Deletes a ticket by its ID.
     */
    void deleteTicket(Long id);
}
